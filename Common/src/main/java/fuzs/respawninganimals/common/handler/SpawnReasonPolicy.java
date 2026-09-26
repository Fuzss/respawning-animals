package fuzs.respawninganimals.common.handler;

import com.google.common.collect.Sets;
import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.config.CommonConfig;
import net.minecraft.world.entity.EntitySpawnReason;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

/**
 * Determines how a mob is handled by the respawning mechanics based on the {@link EntitySpawnReason} it was spawned
 * with.
 */
public enum SpawnReasonPolicy {
    /**
     * The mob is treated as a regular wild animal: it is removed when far away from players and respawns naturally.
     * <p>
     * Jockey mounts are always included, otherwise mounts pile up after their riders have despawned again.
     */
    DESPAWN(EntitySpawnReason.JOCKEY),
    /**
     * The mob is left completely alone: it is neither removed by the mod nor made persistent, so vanilla mechanics
     * (like a mob removing itself after a timer) keep working.
     * <p>
     * Game events are always included as those mobs manage their own despawning and break when made persistent, e.g.
     * skeleton trap horses and trader llamas.
     */
    IGNORE(EntitySpawnReason.EVENT),
    /**
     * The mob is never removed by the mod and is made persistent, so it does not count towards the creature mob cap.
     * Used for mobs that should always remain in the world like player-bred or tamed animals.
     */
    PERSIST;

    private final Set<EntitySpawnReason> spawnReasons;

    SpawnReasonPolicy(EntitySpawnReason... spawnReasons) {
        this.spawnReasons = Sets.immutableEnumSet(Arrays.asList(spawnReasons));
    }

    public boolean matches(EntitySpawnReason spawnReason) {
        return this.spawnReasons.contains(spawnReason);
    }

    /**
     * Only a small allowlist of spawn reasons is affected by the respawning mechanics, everything else - including
     * unknown reasons and mobs that never had a reason set - is treated as persistent.
     */
    public static SpawnReasonPolicy fromSpawnReason(@Nullable EntitySpawnReason spawnReason) {
        if (spawnReason == null) {
            return PERSIST;
        }

        for (SpawnReasonPolicy spawnReasonPolicy : values()) {
            if (spawnReasonPolicy.matches(spawnReason)) {
                return spawnReasonPolicy;
            }
        }

        if (RespawningAnimals.CONFIG.get(CommonConfig.class).volatileDespawnReasons.contains(spawnReason)) {
            return DESPAWN;
        } else {
            return PERSIST;
        }
    }
}
