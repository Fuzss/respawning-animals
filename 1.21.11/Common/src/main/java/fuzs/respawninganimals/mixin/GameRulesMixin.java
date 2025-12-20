package fuzs.respawninganimals.mixin;

import fuzs.respawninganimals.init.ModRegistry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.class)
abstract class GameRulesMixin {
    @Shadow
    @Final
    private GameRuleMap rules;

    @Inject(method = "<init>(Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/world/level/gamerules/GameRuleMap;)V",
            at = @At("TAIL"))
    public void init(FeatureFlagSet featureFlagSet, GameRuleMap gameRuleMap, CallbackInfo callback) {
        // if the game rule is not present (this is a world which has been loaded without the mod before) set value
        // to true instead of default false to prevent unwanted behavior such as animals vanishing from farms
        if (!gameRuleMap.has(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value())) {
            this.rules.set(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value(), Boolean.FALSE);
        }
    }
}
