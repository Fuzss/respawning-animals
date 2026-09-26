package fuzs.respawninganimals.common.entity;

import net.minecraft.world.entity.EntitySpawnReason;
import org.jspecify.annotations.Nullable;

/**
 * Implemented on {@link net.minecraft.world.entity.Mob} by each mod loader to mirror the mod owned
 * {@link SpawnReasonMob#respawninganimals$getSpawnReason() spawn reason} into the loader-specific field, so other mods
 * reading the loader API observe the same value.
 */
public interface SpawnReasonSetter {
    void respawninganimals$copySpawnReason(@Nullable EntitySpawnReason spawnReason);
}
