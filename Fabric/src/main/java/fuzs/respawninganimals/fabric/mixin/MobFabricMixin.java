package fuzs.respawninganimals.fabric.mixin;

import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.fabric.RespawningAnimalsFabric;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Run before Fabric Api to give that a chance to override the serialized spawn reason after us in case it is ever
 * implemented on their end.
 */
@Mixin(value = Mob.class, priority = 1500)
abstract class MobFabricMixin extends LivingEntity {

    protected MobFabricMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(ValueOutput output, CallbackInfo callback) {
        output.storeNullable(RespawningAnimalsFabric.SPAWN_REASON_TAG,
                RespawningAnimalsFabric.SPAWN_REASON_CODEC,
                this.spawnReason());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(ValueInput input, CallbackInfo callback) {
        input.read(RespawningAnimalsFabric.SPAWN_REASON_TAG, RespawningAnimalsFabric.SPAWN_REASON_CODEC)
                .ifPresent((EntitySpawnReason spawnReason) -> {
                    try {
                        Class<?> clazz = Class.forName("net.fabricmc.fabric.impl.event.lifecycle.EntityLoadDataSetter");
                        MethodType methodType = MethodType.methodType(void.class, EntitySpawnReason.class);
                        MethodHandle handle = MethodHandles.publicLookup()
                                .findVirtual(clazz, "fabric_setSpawnReason", methodType);
                        handle.invoke(this, spawnReason);
                    } catch (Throwable throwable) {
                        RespawningAnimals.LOGGER.warn("Unable to set spawn reason {} for {}: {}",
                                spawnReason,
                                this,
                                throwable.getMessage());
                    }
                });
    }
}
