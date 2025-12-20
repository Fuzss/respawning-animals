# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v21.11.0-1.21.11] - 2025-12-20

### Added

- Add game rule `respawninganimals:remove_animals_distance`: Animals beyond this distance from the nearest player may be
  removed over time
- Add game rule `respawninganimals:remove_animals_instantly_distance`: Animals beyond this distance from the nearest
  player are removed instantly

### Changed

- Update to Minecraft 1.21.11
- Rename game rule `persistentAnimals` to `respawninganimals:remove_animals_when_far_away` (inverted)
- Rename game rule `animalMobCap` to `respawninganimals:min_animals_near_player`
