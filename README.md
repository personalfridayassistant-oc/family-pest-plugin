# VitaLite Quest Automation Plugins

This repository contains VitaLite external plugins for automating OSRS quest and miniquest progression.

## Included plugins

### 1) Family Pest Miniquest Plugin

Automates the Family Pest miniquest flow:
- Checks Family Crest prerequisite
- Visits all Fitzharmon brothers
- Returns to Dimintheis for completion

### 2) Cook's Assistant Auto Plugin

Automates Cook's Assistant completion once required ingredients are in inventory:
- 1x Egg
- 1x Bucket of milk
- 1x Pot of flour

The plugin will:
- verify ingredients,
- walk to the Lumbridge castle kitchen,
- handle dialogue with the Cook,
- complete quest hand-in.

## Build

```bash
gradlew jar
```

## Notes

- Plugin architecture is state-machine based via `VitaPlugin`.
- See `docs/quest-selection.md` for the quest selection and implementation rationale.
