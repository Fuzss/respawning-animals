package fuzs.respawninganimals;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.EntityTickEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.living.AnimalTameCallback;
import fuzs.puzzleslib.common.api.event.v1.entity.living.CheckMobDespawnCallback;
import fuzs.puzzleslib.common.api.event.v1.level.GatherPotentialSpawnsCallback;
import fuzs.puzzleslib.common.api.event.v1.server.GameRuleUpdatedCallback;
import fuzs.puzzleslib.common.api.event.v1.server.ServerLifecycleEvents;
import fuzs.respawninganimals.handler.AnimalPersistenceHandler;
import fuzs.respawninganimals.handler.AnimalSpawningHandler;
import fuzs.respawninganimals.init.ModRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RespawningAnimals implements ModConstructor {
    public static final String MOD_ID = "respawninganimals";
    public static final String MOD_NAME = "Respawning Animals";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EntityTickEvents.END.register(AnimalPersistenceHandler::onEndEntityTick);
        AnimalTameCallback.EVENT.register(AnimalPersistenceHandler::onAnimalTame);
        EntityRidingEvents.START.register(AnimalPersistenceHandler::onStartRiding);
        CheckMobDespawnCallback.EVENT.register(AnimalSpawningHandler::onCheckMobDespawn);
        ServerEntityLevelEvents.LOAD.register(AnimalSpawningHandler::onEntityLoad);
        ServerLifecycleEvents.STARTED.register(AnimalSpawningHandler::onServerStarted);
        GatherPotentialSpawnsCallback.EVENT.register(AnimalSpawningHandler::onGatherPotentialSpawns);
    }

    @Override
    public void onCommonSetup() {
        GameRuleUpdatedCallback.gameRuleUpdated(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value())
                .register((MinecraftServer minecraftServer, GameRule<Boolean> gameRule, Boolean newGameRuleValue) -> {
                    AnimalSpawningHandler.onGameRulesUpdated(minecraftServer.overworld().getGameRules());
                });
        GameRuleUpdatedCallback.gameRuleUpdated(ModRegistry.MIN_ANIMALS_NEAR_PLAYER_GAME_RULE.value())
                .register((MinecraftServer minecraftServer, GameRule<Integer> gameRule, Integer newGameRuleValue) -> {
                    AnimalSpawningHandler.onGameRulesUpdated(minecraftServer.overworld().getGameRules());
                });
        GameRuleUpdatedCallback.gameRuleUpdated(ModRegistry.REMOVE_ANIMALS_DISTANCE_GAME_RULE.value())
                .register((MinecraftServer minecraftServer, GameRule<Integer> gameRule, Integer newGameRuleValue) -> {
                    AnimalSpawningHandler.onGameRulesUpdated(minecraftServer.overworld().getGameRules());
                });
        GameRuleUpdatedCallback.gameRuleUpdated(ModRegistry.REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE.value())
                .register((MinecraftServer minecraftServer, GameRule<Integer> gameRule, Integer newGameRuleValue) -> {
                    AnimalSpawningHandler.onGameRulesUpdated(minecraftServer.overworld().getGameRules());
                });
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
