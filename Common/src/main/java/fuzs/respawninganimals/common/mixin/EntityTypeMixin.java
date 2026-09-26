package fuzs.respawninganimals.common.mixin;

import fuzs.respawninganimals.common.entity.SpawnReasonMob;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Captures the spawn reason for every entity created through {@link EntityType#create(Level, EntitySpawnRequest)}, the
 * single funnel all entity creation paths go through.
 * <p>
 * This covers spawn reasons that are never passed to {@link Mob#finalizeSpawn}, like bees released from a beehive
 * ({@link EntitySpawnReason#LOAD}) or turtle eggs ({@link EntitySpawnReason#BREEDING}).
 */
@Mixin(EntityType.class)
abstract class EntityTypeMixin {
    @Inject(method = "create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnRequest;)Lnet/minecraft/world/entity/Entity;",
            at = @At("RETURN"))
    public void create(Level level, EntitySpawnRequest request, CallbackInfoReturnable<Entity> callback) {
        if (callback.getReturnValue() instanceof Mob mob) {
            ((SpawnReasonMob) mob).respawninganimals$setSpawnReason(request.reason());
        }
    }
}
