package fuzs.respawninganimals.common.handler;

import fuzs.puzzleslib.common.api.core.v1.ModContainer;
import fuzs.puzzleslib.common.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.init.ModRegistry;
import fuzs.respawninganimals.common.services.CommonAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.gamerules.GameRules;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AnimalSpawningHandler {
    private static final int VANILLA_CREATURE_CATEGORY_MAX_INSTANCES = MobCategory.CREATURE.getMaxInstancesPerChunk();
    private static final int VANILLA_CREATURE_CATEGORY_DESPAWN_DISTANCE = MobCategory.CREATURE.getDespawnDistance();
    private static final int VANILLA_CREATURE_CATEGORY_NO_DESPAWN_DISTANCE = MobCategory.CREATURE.getNoDespawnDistance();
    private static final Set<EntitySpawnReason> PERSISTENT_SPAWN_TYPES = Set.of(EntitySpawnReason.STRUCTURE,
            EntitySpawnReason.BREEDING,
            EntitySpawnReason.TRIGGERED,
            EntitySpawnReason.BUCKET);

    public static void onServerStarted(MinecraftServer minecraftServer) {
        onGameRulesUpdated(minecraftServer.getGameRules());
    }

    public static void onGameRulesUpdated(GameRules gameRules) {
        // This setting removes a 400-tick cooldown between spawn cycles.
        // Only creatures have this, all other categories don't.
        MobCategory.CREATURE.isPersistent = !gameRules.get(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value());
        MobCategory.CREATURE.max = MobCategory.CREATURE.isPersistent ? VANILLA_CREATURE_CATEGORY_MAX_INSTANCES :
                gameRules.get(ModRegistry.MIN_ANIMALS_NEAR_PLAYER_GAME_RULE.value());
        MobCategory.CREATURE.noDespawnDistance =
                MobCategory.CREATURE.isPersistent ? VANILLA_CREATURE_CATEGORY_NO_DESPAWN_DISTANCE :
                        gameRules.get(ModRegistry.REMOVE_ANIMALS_DISTANCE_GAME_RULE.value());
        MobCategory.CREATURE.despawnDistance =
                MobCategory.CREATURE.isPersistent ? VANILLA_CREATURE_CATEGORY_DESPAWN_DISTANCE :
                        gameRules.get(ModRegistry.REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE.value());
    }

    public static EventResult onCheckMobDespawn(Mob mob, ServerLevel serverLevel) {
        if (isAllowedToDespawn(mob, serverLevel.getGameRules())) {
            // copied from Mob::checkDespawn, so we can run it manually for the creature mob category
            if (!mob.isPersistenceRequired() && !mob.requiresCustomPersistence()) {
                Player player = serverLevel.getNearestPlayer(mob, -1.0);
                if (player != null) {
                    double distanceToSqr = player.distanceToSqr(mob);
                    MobCategory mobCategory = mob.getType().getCategory();
                    int despawnDistance = mobCategory.getDespawnDistance();
                    int despawnDistanceSqr = despawnDistance * despawnDistance;
                    if (distanceToSqr > despawnDistanceSqr) {
                        return EventResult.ALLOW;
                    }

                    int noDespawnDistance = mobCategory.getNoDespawnDistance();
                    int noDespawnDistanceSqr = noDespawnDistance * noDespawnDistance;
                    if (mob.getNoActionTime() > 600 && mob.getRandom().nextInt(800) == 0
                            && distanceToSqr > noDespawnDistanceSqr) {
                        return EventResult.ALLOW;
                    } else {
                        if (distanceToSqr < noDespawnDistanceSqr) {
                            mob.setNoActionTime(0);
                        }

                        // since this involves random don't let vanilla run again, we covered everything
                        return EventResult.DENY;
                    }
                }
            } else {
                mob.setNoActionTime(0);
            }
        }

        return EventResult.PASS;
    }

    public static boolean isAllowedToDespawn(Mob mob, @Nullable GameRules gameRules) {
        if (isAnimalDespawningAllowed(mob.getType(), gameRules, mob.getType().getCategory())) {
            EntitySpawnReason spawnReason = CommonAbstractions.INSTANCE.getEntitySpawnReason(mob);
            return spawnReason != null && !PERSISTENT_SPAWN_TYPES.contains(spawnReason);
        } else {
            return false;
        }
    }

    public static boolean isAnimalDespawningAllowed(EntityType<?> entityType, @Nullable GameRules gameRules, MobCategory mobCategory) {
        if (gameRules != null && !gameRules.get(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value())) {
            return false;
        } else if (entityType.builtInRegistryHolder().is(ModRegistry.PERSISTENT_ANIMALS_ENTITY_TYPE_TAG)) {
            return false;
        } else {
            return mobCategory == MobCategory.CREATURE;
        }
    }

    public static EventResult onEntityJoin(Entity entity, ServerLevel serverLevel, boolean isLoadedFromDisk, @Nullable EntitySpawnReason spawnReason) {
        if (!isLoadedFromDisk) {
            // don't spawn mobs during chunk generation which we would remove again anyway since they are certainly too far from the player
            if (spawnReason == EntitySpawnReason.CHUNK_GENERATION) {
                // chunk generation only runs for the creature type, so we can safely fix the type if necessary
                applyCorrectMobCategory(entity.getType());
                if (isAnimalDespawningAllowed(entity.getType(), serverLevel.getGameRules(), MobCategory.CREATURE)) {
                    return EventResult.INTERRUPT;
                }
            }
        }

        // make existing mobs in the world persistent to help with compat for worlds that have been used without the mod before
        setPersistenceForPersistentAnimal(entity);
        return EventResult.PASS;
    }

    private static void setPersistenceForPersistentAnimal(Entity entity) {
        // find all mobs that would count towards the creature mob cap and therefore would hinder the spawn cycle from spawning new animals
        // making them persistent prevents counting towards the mob cap, otherwise this doesn't really have any implications for us since we ignore those spawn types anyway
        // in vanilla if the mod were to be removed, this also has no consequences
        if (entity instanceof Mob mob && mob.getType().getCategory() == MobCategory.CREATURE) {
            // do not check the game rule, in case it is toggled during gameplay
            if (!mob.isPersistenceRequired() && !isAllowedToDespawn(mob, null)) {
                mob.setPersistenceRequired();
            }
        }
    }

    private static void applyCorrectMobCategory(EntityType<?> entityType) {
        // an entity type must have the same mob category set that is used for spawning the entity naturally (via mob spawn type natural or chunk generation)
        // otherwise the entity does not count towards its own spawn cap, which can lead to infinite spawns
        // for creatures this unfortunately usually goes unnoticed since the spawning cycle never runs as there are usually enough vanilla animals in the world to fill up the cap
        if (entityType.getCategory() != MobCategory.CREATURE) {
            Identifier identifier = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            Optional<String> issues = ModLoaderEnvironment.INSTANCE.getModContainer(identifier.getNamespace())
                    .map(modContainer -> modContainer.getContactTypes().get("issues"));
            String modName = ModLoaderEnvironment.INSTANCE.getModContainer(identifier.getNamespace())
                    .map(ModContainer::getDisplayName)
                    .orElse(identifier.getNamespace());
            RespawningAnimals.LOGGER.warn(
                    "Mismatched spawn type for {}! Mob is registered as {}, but spawning as {}. Report this to the author of {}"
                            + (issues.map((String s) -> " at " + s).orElse("")) + ".",
                    identifier,
                    entityType.getCategory(),
                    MobCategory.CREATURE,
                    modName);
            entityType.category = MobCategory.CREATURE;
        }
    }

    public static void onGatherPotentialSpawns(ServerLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, MobCategory mobCategory, BlockPos blockPos, List<Weighted<MobSpawnSettings.SpawnerData>> mobs) {
        if (mobCategory == MobCategory.CREATURE) {
            Iterator<Weighted<MobSpawnSettings.SpawnerData>> iterator = mobs.iterator();
            while (iterator.hasNext()) {
                Weighted<MobSpawnSettings.SpawnerData> spawnerData = iterator.next();
                applyCorrectMobCategory(spawnerData.value().type());
                // prevent blacklisted animals from being respawned to prevent them from spawning endlessly since they also do not count towards the mob cap
                if (!isAnimalDespawningAllowed(spawnerData.value().type(),
                        level.getGameRules(),
                        MobCategory.CREATURE)) {
                    iterator.remove();
                }
            }
        }
    }
}
