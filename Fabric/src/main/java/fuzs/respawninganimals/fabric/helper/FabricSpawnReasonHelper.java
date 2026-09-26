package fuzs.respawninganimals.fabric.helper;

import fuzs.respawninganimals.common.RespawningAnimals;
import net.minecraft.world.entity.EntitySpawnReason;
import org.jspecify.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Mirrors the mod owned spawn reason into Fabric Api's internal field, so that other mods reading
 * {@code EntityLoadData#spawnReason()} observe the same value. Fabric API only sets that field when the entity is
 * created, this also covers the reason refined by {@code Mob#finalizeSpawn} and the value restored from disk.
 * <p>
 * This accesses an implementation package on purpose, so it is done via reflection and fails gracefully if Fabric API
 * ever changes their implementation.
 */
public final class FabricSpawnReasonHelper {
    private static final @Nullable MethodHandle SET_SPAWN_REASON = findSetter();

    private FabricSpawnReasonHelper() {
        // NO-OP
    }

    private static @Nullable MethodHandle findSetter() {
        try {
            Class<?> clazz = Class.forName("net.fabricmc.fabric.impl.event.lifecycle.EntityLoadDataSetter");
            MethodType methodType = MethodType.methodType(void.class, EntitySpawnReason.class);
            return MethodHandles.publicLookup().findVirtual(clazz, "fabric_setSpawnReason", methodType);
        } catch (Throwable throwable) {
            RespawningAnimals.LOGGER.warn("Unable to resolve Fabric API spawn reason setter: {}",
                    throwable.getMessage());
            return null;
        }
    }

    public static void setSpawnReason(Object entity, @Nullable EntitySpawnReason spawnReason) {
        if (SET_SPAWN_REASON != null) {
            try {
                SET_SPAWN_REASON.invoke(entity, spawnReason);
            } catch (Throwable throwable) {
                RespawningAnimals.LOGGER.warn("Unable to set Fabric API spawn reason {} for {}: {}",
                        spawnReason,
                        entity,
                        throwable.getMessage());
            }
        }
    }
}
