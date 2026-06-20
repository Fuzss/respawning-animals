package fuzs.respawninganimals.neoforge.services;

import fuzs.respawninganimals.common.services.CommonAbstractions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;

public final class NeoForgeAbstractions implements CommonAbstractions {
    @Override
    public EntitySpawnReason getEntitySpawnReason(Mob mob) {
        return mob.getSpawnType();
    }
}
