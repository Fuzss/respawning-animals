# Respawning Animals

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects) and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/respawninganimals/banner.png)

## Configuration

Remove Animals is fully controlled through game rules and entity tags. No other configuration options are provided.

All rules are world-specific and can be set initially during world creation via the game rules menu, or changed at any time using the `/gamerule` command.

---

### Game rules

#### `respawninganimals:remove_animals_when_far_away`
**Default:** `true` for new worlds, `false` for existing worlds

Controls whether animals are removed when they are far away from players.

When enabled, animals no longer stay in the world forever. Animals that are far away from any player may be removed, similar to monsters and water creatures. Animals become permanent only after player interaction such as feeding, leashing, riding, or naming.

When disabled, animals behave like in vanilla Minecraft and are never removed due to distance.

This rule must be enabled for all other rules below to have any effect.

This rule was known as `persistentAnimals` before Minecraft 1.21.11.

---

#### `respawninganimals:min_animals_near_player`
**Default:** `15`

If the number of animals near a player falls below this value, new animals will spawn nearby to reach the minimum.

This helps keep areas populated when animals are removed over time.

Only applies when `remove_animals_when_far_away` is enabled.

This rule was known as `animalMobCap` before Minecraft 1.21.11.

---

#### `respawninganimals:remove_animals_distance`
**Default:** `32`

Animals farther than this distance from the nearest player may be removed over time.

Only applies when `remove_animals_when_far_away` is enabled.

---

#### `respawninganimals:remove_animals_instantly_distance`
**Default:** `128`

Animals farther than this distance from the nearest player are removed instantly.

This acts as a hard cutoff to prevent animals from accumulating far away from players.

Only applies when `remove_animals_when_far_away` is enabled.

---

### Entity tags

#### `respawninganimals:persistent_animals`

It is possible to exclude specific animal types from being affected by these rules.

Any entity type added to this tag will never be removed, regardless of game rule settings.

This allows fine-grained control for mods, datapacks, or special animals that should always remain in the world.
