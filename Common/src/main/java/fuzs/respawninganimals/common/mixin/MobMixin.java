package fuzs.respawninganimals.common.mixin;

import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.world.entity.SpawnReasonMob;
import fuzs.respawninganimals.common.world.entity.SpawnReasonSetter;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
abstract class MobMixin implements SpawnReasonMob {
    @Unique
    @Nullable
    private EntitySpawnReason respawninganimals$spawnReason;

    @Override
    @Nullable
    public EntitySpawnReason respawninganimals$getSpawnReason() {
        return this.respawninganimals$spawnReason;
    }

    @Override
    public void respawninganimals$setSpawnReason(@Nullable EntitySpawnReason spawnReason) {
        this.respawninganimals$spawnReason = spawnReason;
        if (this instanceof SpawnReasonSetter setter) {
            setter.respawninganimals$copySpawnReason(spawnReason);
        }
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData, CallbackInfoReturnable<@Nullable SpawnGroupData> callback) {
        // Conversions inherit the original spawn reason, so do not overwrite an already inherited one.
        if (spawnReason != EntitySpawnReason.CONVERSION || this.respawninganimals$spawnReason == null) {
            this.respawninganimals$setSpawnReason(spawnReason);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo callback) {
        output.storeNullable(RespawningAnimals.SPAWN_REASON_TAG,
                RespawningAnimals.SPAWN_REASON_CODEC,
                this.respawninganimals$spawnReason);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput input, CallbackInfo callback) {
        EntitySpawnReason spawnReason = input.read(RespawningAnimals.SPAWN_REASON_TAG,
                        RespawningAnimals.SPAWN_REASON_CODEC)
                .orElseGet(() -> input.read(RespawningAnimals.LEGACY_SPAWN_REASON_TAG,
                        RespawningAnimals.LEGACY_SPAWN_REASON_CODEC).orElse(null));
        if (spawnReason != null) {
            this.respawninganimals$setSpawnReason(spawnReason);
        }
    }
}
