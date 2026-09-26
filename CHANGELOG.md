# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v26.1.1-mc26.1.x] - 2026-09-27

### Added

- Add a config file for choosing which spawn reasons are affected by the respawning mechanics, and which player
  interactions make an animal persistent

### Changed

- Track the actual spawn reason per mob, including conversions inheriting the original reason
- Leave mobs spawned by game events entirely to vanilla, so their own despawning logic keeps working
- Always remove jockey mounts when far away to prevent mounts from piling up after their riders have despawned

## [v26.1.0-mc26.1.x] - 2026-04-27

### Changed

- Update to Minecraft 26.1.x
