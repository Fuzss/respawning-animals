package fuzs.respawninganimals.neoforge.client;

import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.client.RespawningAnimalsClient;
import fuzs.respawninganimals.common.data.client.ModLanguageProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = RespawningAnimals.MOD_ID, dist = Dist.CLIENT)
public class RespawningAnimalsNeoForgeClient {

    public RespawningAnimalsNeoForgeClient() {
        ClientModConstructor.construct(RespawningAnimals.MOD_ID, RespawningAnimalsClient::new);
        DataProviderBuilder.of(RespawningAnimals.MOD_ID).addProvider(ModLanguageProvider::new);
    }
}
