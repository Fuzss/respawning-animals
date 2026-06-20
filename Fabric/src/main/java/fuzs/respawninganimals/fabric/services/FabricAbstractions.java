package fuzs.respawninganimals.fabric.services;

import fuzs.respawninganimals.common.services.CommonAbstractions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;

public final class FabricAbstractions implements CommonAbstractions {
    @Override
    public EntitySpawnReason getEntitySpawnReason(Mob mob) {
        return mob.spawnReason();
    }
}
