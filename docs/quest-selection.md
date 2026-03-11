# Quest Automation Analysis

## VitaLite automation model (observed)

From this repository's existing plugin implementation, VitaLite external plugins are structured around:

- `VitaPlugin` loop-driven state machines for deterministic progression.
- High-level APIs for game interaction (`Walker`, `InventoryAPI`, `Dialogues`, `NpcQuery`).
- Config + overlay components for runtime observability.
- Resumable progression by polling quest vars / quest state each tick.

## Family Pest implementation patterns reused

The Family Pest plugin demonstrates patterns that were intentionally mirrored for a new quest plugin:

1. Enum-based state machine.
2. Requirement checks before travel/dialogue.
3. Pathfinding-first movement (`Walker`) with dialogue automation.
4. Lightweight overlay exposing current state and readiness.

## Old School RuneScape quest selection process

I reviewed the OSRS quest catalogue from an automation-feasibility perspective and prioritized quests that are:

- low combat risk,
- low branching dialogue complexity,
- single-location hand-in compatible,
- deterministic completion with known inventory requirements.

### Shortlist

- Cook's Assistant
- Sheep Shearer
- Doric's Quest
- Witch's Potion
- Romeo & Juliet

### Selected quest: **Cook's Assistant**

Cook's Assistant was selected because it is highly deterministic and maps cleanly to the Family Pest plugin style:

- direct talk-to NPC loop,
- simple inventory requirement gate,
- minimal travel,
- straightforward dialogue flow.

## Implementation scope

The new plugin automates quest completion when required ingredients are already in inventory:

- 1x Egg
- 1x Bucket of milk
- 1x Pot of flour

This mirrors the Family Pest plugin's requirement-first approach (it automates progression, not full resource gathering).
