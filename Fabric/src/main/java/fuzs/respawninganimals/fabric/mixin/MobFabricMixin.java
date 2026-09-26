package fuzs.respawninganimals.fabric.mixin;

import fuzs.respawninganimals.common.entity.SpawnReasonSetter;
import fuzs.respawninganimals.fabric.helper.FabricSpawnReasonHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
abstract class MobFabricMixin extends LivingEntity implements SpawnReasonSetter {

    protected MobFabricMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void respawninganimals$copySpawnReason(@Nullable EntitySpawnReason spawnReason) {
        FabricSpawnReasonHelper.setSpawnReason(this, spawnReason);
    }
}
