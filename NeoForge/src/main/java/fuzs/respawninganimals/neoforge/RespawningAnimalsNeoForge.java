package fuzs.respawninganimals.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.data.tags.ModEntityTypesTagProvider;
import fuzs.respawninganimals.common.world.entity.SpawnReasonMob;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

@Mod(RespawningAnimals.MOD_ID)
public class RespawningAnimalsNeoForge {

    public RespawningAnimalsNeoForge() {
        ModConstructor.construct(RespawningAnimals.MOD_ID, RespawningAnimals::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderBuilder.of(RespawningAnimals.MOD_ID).addProvider(ModEntityTypesTagProvider::new);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final FinalizeSpawnEvent event) -> {
            // Covers mods that override Mob::finalizeSpawn without calling super, where the mod-owned spawn reason
            // cannot be captured by the mixin on Mob::finalizeSpawn.
            // This also mirrors the reason into Forge's own spawn type field.
            ((SpawnReasonMob) event.getEntity()).respawninganimals$setSpawnReason(event.getSpawnType());
        });
    }
}
