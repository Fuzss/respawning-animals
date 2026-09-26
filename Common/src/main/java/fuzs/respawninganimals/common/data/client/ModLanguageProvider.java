package fuzs.respawninganimals.common.data.client;

import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import fuzs.respawninganimals.common.init.ModRegistry;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        this.add(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value(), "Remove animals when far away");
        this.addGameRuleDescription(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value(),
                "Animals are removed when far away from players unless made permanent by interactions such as feeding, leashing, riding, or naming.");
        this.add(ModRegistry.MIN_ANIMALS_NEAR_PLAYER_GAME_RULE.value(), "Minimum animals near player");
        this.addGameRuleDescription(ModRegistry.MIN_ANIMALS_NEAR_PLAYER_GAME_RULE.value(),
                "Spawn animals near each player if less than this value exist locally. Only applies when animals can be removed.");
        this.add(ModRegistry.REMOVE_ANIMALS_DISTANCE_GAME_RULE.value(), "Animal removal distance");
        this.addGameRuleDescription(ModRegistry.REMOVE_ANIMALS_DISTANCE_GAME_RULE.value(),
                "Animals beyond this distance from the nearest player may be removed over time. Only applies when animals can be removed.");
        this.add(ModRegistry.REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE.value(), "Instant animal removal distance");
        this.addGameRuleDescription(ModRegistry.REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE.value(),
                "Animals beyond this distance from the nearest player are removed instantly. Only applies when animals can be removed.");
    }
}
