package fuzs.respawninganimals.common.world.entity;

import net.minecraft.world.entity.EntitySpawnReason;
import org.jspecify.annotations.Nullable;

/**
 * Implemented on {@link net.minecraft.world.entity.Mob} to store the {@link EntitySpawnReason} the mob was spawned
 * with.
 * <p>
 * The value is captured when the entity is created and refined when
 * {@link net.minecraft.world.entity.Mob#finalizeSpawn} is called.
 */
public interface SpawnReasonMob {
    @Nullable EntitySpawnReason respawninganimals$getSpawnReason();

    void respawninganimals$setSpawnReason(@Nullable EntitySpawnReason spawnReason);
}
