package fuzs.respawninganimals.common.mixin;

import fuzs.respawninganimals.common.world.entity.SpawnReasonMob;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.ConversionType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Conversions inherit the spawn reason of the original mob. The conversion reason itself is only used as a marker
 * and is replaced by the source mob's reason, e.g. shearing a naturally spawned mushroom cow results in a naturally
 * spawned cow.
 */
@Mixin(ConversionType.class)
abstract class ConversionTypeMixin {
    @Inject(method = "convertCommon", at = @At("RETURN"))
    private static void respawninganimals$inheritSpawnReason(Mob from, Mob to, ConversionParams params, CallbackInfo callback) {
        SpawnReasonMob fromHolder = (SpawnReasonMob) from;
        SpawnReasonMob toHolder = (SpawnReasonMob) to;
        if (toHolder.respawninganimals$getSpawnReason() == EntitySpawnReason.CONVERSION) {
            toHolder.respawninganimals$setSpawnReason(fromHolder.respawninganimals$getSpawnReason());
        }
    }
}
