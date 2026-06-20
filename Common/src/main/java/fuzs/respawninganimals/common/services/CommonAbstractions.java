package fuzs.respawninganimals.common.services;

import fuzs.puzzleslib.common.api.core.v1.ServiceProviderHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = ServiceProviderHelper.load(CommonAbstractions.class);

    EntitySpawnReason getEntitySpawnReason(Mob mob);
}
