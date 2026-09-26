package fuzs.respawninganimals.neoforge.mixin;

import fuzs.respawninganimals.common.world.entity.SpawnReasonSetter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
abstract class MobNeoForgeMixin extends LivingEntity implements SpawnReasonSetter {
    @Shadow
    @Nullable
    private EntitySpawnReason spawnType;

    protected MobNeoForgeMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void respawninganimals$copySpawnReason(@Nullable EntitySpawnReason spawnReason) {
        this.spawnType = spawnReason;
    }
}
