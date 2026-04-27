package fuzs.respawninganimals.init;

import fuzs.puzzleslib.common.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.common.api.init.v3.tags.TagFactory;
import fuzs.respawninganimals.RespawningAnimals;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(RespawningAnimals.MOD_ID);
    public static final Holder.Reference<GameRule<Boolean>> REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE = REGISTRIES.registerGameRule(
            "remove_animals_when_far_away",
            GameRuleCategory.SPAWNING,
            true);
    public static final Holder.Reference<GameRule<Integer>> MIN_ANIMALS_NEAR_PLAYER_GAME_RULE = REGISTRIES.registerGameRule(
            "min_animals_near_player",
            GameRuleCategory.SPAWNING,
            15,
            0,
            100);
    public static final Holder.Reference<GameRule<Integer>> REMOVE_ANIMALS_DISTANCE_GAME_RULE = REGISTRIES.registerGameRule(
            "remove_animals_distance",
            GameRuleCategory.SPAWNING,
            32,
            0,
            512);
    public static final Holder.Reference<GameRule<Integer>> REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE = REGISTRIES.registerGameRule(
            "remove_animals_instantly_distance",
            GameRuleCategory.SPAWNING,
            128,
            0,
            512);

    static final TagFactory TAGS = TagFactory.make(RespawningAnimals.MOD_ID);
    public static final TagKey<EntityType<?>> PERSISTENT_ANIMALS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "persistent_animals");

    public static void bootstrap() {
        // NO-OP
    }
}
