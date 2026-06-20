package fuzs.respawninganimals.fabric;

import com.mojang.serialization.Codec;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.util.v1.CodecExtras;
import fuzs.respawninganimals.common.RespawningAnimals;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.EntitySpawnReason;

public class RespawningAnimalsFabric implements ModInitializer {
    public static final String SPAWN_REASON_TAG = RespawningAnimals.id("spawn_reason").toString();
    public static final Codec<EntitySpawnReason> SPAWN_REASON_CODEC = CodecExtras.fromEnum(EntitySpawnReason.class);

    @Override
    public void onInitialize() {
        ModConstructor.construct(RespawningAnimals.MOD_ID, RespawningAnimals::new);
    }
}
