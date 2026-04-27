package fuzs.respawninganimals.fabric.client;

import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.client.RespawningAnimalsClient;
import net.fabricmc.api.ClientModInitializer;

public class RespawningAnimalsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(RespawningAnimals.MOD_ID, RespawningAnimalsClient::new);
    }
}
