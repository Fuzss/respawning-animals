package fuzs.respawninganimals.common.config;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;
import net.minecraft.world.entity.EntitySpawnReason;

import java.util.Set;

public class CommonConfig implements ConfigCore {
    @Config(description = {
            "Types of animal spawns that are affected by the respawning mechanics, meaning their animals may be removed when they are far away from players.",
            "Animals not covered by any enabled option are never removed on their own, but can still be made persistent through player interaction like breeding, leading or naming.",
            "Only applies when the \"remove_animals_when_far_away\" game rule is enabled."
    })
    final SpawnReasonsConfig despawnReasons = new SpawnReasonsConfig();
    @Config(description = {
            "Player interactions that make an animal persistent so it is never removed by the respawning mechanics.",
            "Unlike the spawn reason options disabling these can never lead to animals piling up, as they only ever mark a single animal as persistent."
    })
    public final PersistenceActionsConfig persistenceActions = new PersistenceActionsConfig();

    public Set<EntitySpawnReason> volatileDespawnReasons = Set.of();

    @Override
    public void afterConfigReload() {
        this.volatileDespawnReasons = this.despawnReasons.toSet();
    }

    private static class SpawnReasonsConfig implements ConfigCore {
        @Config(description = "Animals that spawn naturally in the world, e.g. through world generation or regular mob spawning.")
        public boolean worldGen = true;
        @Config(description = "Animals spawned by mob spawners, including trial spawners.")
        public boolean spawner = false;
        @Config(description = "Animals spawned directly by a player, e.g. via a spawn egg, a dispenser or the /summon command.")
        public boolean playerSpawn = false;
        @Config(description = "Animals created through breeding, hatched from eggs or released by a bucket.")
        public boolean husbandry = false;
        @Config(description = "Animals spawned by structures, e.g. cats in witch huts or allays in woodland mansions.")
        public boolean scripted = false;

        public Set<EntitySpawnReason> toSet() {
            ImmutableSet.Builder<EntitySpawnReason> builder = ImmutableSet.builder();

            if (this.worldGen) {
                builder.add(EntitySpawnReason.NATURAL);
                builder.add(EntitySpawnReason.CHUNK_GENERATION);
            }

            if (this.spawner) {
                builder.add(EntitySpawnReason.SPAWNER);
                builder.add(EntitySpawnReason.TRIAL_SPAWNER);
            }

            if (this.playerSpawn) {
                builder.add(EntitySpawnReason.SPAWN_ITEM_USE);
                builder.add(EntitySpawnReason.COMMAND);
                builder.add(EntitySpawnReason.DISPENSER);
            }

            if (this.husbandry) {
                builder.add(EntitySpawnReason.BREEDING);
                builder.add(EntitySpawnReason.BUCKET);
            }

            if (this.scripted) {
                builder.add(EntitySpawnReason.STRUCTURE);
                builder.add(EntitySpawnReason.PATROL);
                builder.add(EntitySpawnReason.MOB_SUMMONED);
                builder.add(EntitySpawnReason.REINFORCEMENT);
                builder.add(EntitySpawnReason.TRIGGERED);
            }

            return builder.build();
        }
    }

    public static class PersistenceActionsConfig implements ConfigCore {
        @Config(description = "Animals that are in love after having been fed their breeding item.")
        public boolean breeding = true;
        @Config(description = "Animals attached to a lead by a player or a fence knot.")
        public boolean leashed = true;
        @Config(description = "Animals with an owner, like tamed wolves, cats or horses.")
        public boolean owned = true;
        @Config(description = "Animals directly after having been tamed.")
        public boolean tamed = true;
        @Config(description = "Animals a player has ridden.")
        public boolean ridden = true;
        @Config(description = "Animals that have entered a vehicle like a boat or minecart.")
        public boolean vehicles = true;
    }
}
