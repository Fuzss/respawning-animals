package fuzs.respawninganimals.common.data.client;

import fuzs.puzzleslib.common.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.respawninganimals.common.init.ModRegistry;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value(), "Remove animals when far away");
        builder.addGameRuleDescription(ModRegistry.REMOVE_ANIMALS_WHEN_FAR_AWAY_GAME_RULE.value(),
                "Animals are removed when far away from players unless made permanent by interactions such as feeding, leashing, riding, or naming.");
        builder.add(ModRegistry.MIN_ANIMALS_NEAR_PLAYER_GAME_RULE.value(), "Minimum animals near player");
        builder.addGameRuleDescription(ModRegistry.MIN_ANIMALS_NEAR_PLAYER_GAME_RULE.value(),
                "Spawn animals near each player if less than this value exist locally. Only applies when animals can be removed.");
        builder.add(ModRegistry.REMOVE_ANIMALS_DISTANCE_GAME_RULE.value(), "Animal removal distance");
        builder.addGameRuleDescription(ModRegistry.REMOVE_ANIMALS_DISTANCE_GAME_RULE.value(),
                "Animals beyond this distance from the nearest player may be removed over time. Only applies when animals can be removed.");
        builder.add(ModRegistry.REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE.value(), "Instant animal removal distance");
        builder.addGameRuleDescription(ModRegistry.REMOVE_ANIMALS_INSTANTLY_DISTANCE_GAME_RULE.value(),
                "Animals beyond this distance from the nearest player are removed instantly. Only applies when animals can be removed.");
    }
}
