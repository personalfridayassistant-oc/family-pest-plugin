# Quest Automation Analysis and Expansion Plan

## 1) VitaLite plugin model (based on this repository)

The existing plugins show a consistent VitaLite architecture that should be reused for any new automated quest plugin:

- **`VitaPlugin` loop + explicit state enum** for deterministic quest progression and easy resume behavior after interruptions.
- **High-level interaction APIs** (`Walker`, `NpcQuery`, `InventoryAPI`, `Dialogues`, `VarAPI`) instead of low-level packet/event handling.
- **Tick-aware control flow** via `Delays.tick(...)` and `onGameTick` polling where varbits are required.
- **Overlay + config pairing** for live observability and user-tunable behavior.

This pattern is visible in both quest implementations already in the repo and should be treated as the baseline template for additional quest plugins.

## 2) Existing plugin analysis

## Family Pest plugin (miniquest)

Strengths and reusable patterns:

1. **Robust state machine coverage** (requirements, multi-target routing, final hand-in).
2. **Quest-var driven resume support** using varbits each game tick.
3. **Travel fallback strategy** (`Walker`, then teleport items, then user guidance).
4. **Dialogue option targeting** with `Dialogues.processDialogues(...)` and branch-specific prompts.
5. **Safety gates** (e.g., coin check before final hand-in).

This is a good model for longer multi-step quests with several NPC destinations.

## Cook's Assistant plugin

Strengths and reusable patterns:

1. **Simple requirement gate** (all required items in inventory before turn-in attempt).
2. **Quest-state short-circuit** to `COMPLETE` when RuneLite quest status is finished.
3. **Low-friction loop**: check requirements -> travel -> talk -> dialogue progression.
4. **Deterministic single-location completion flow**, ideal for beginner quest automation.

This is a good model for short quests with fixed item hand-ins and minimal branching.

## 3) OSRS quest research approach

To choose additional candidates, I reviewed the OSRS quest catalogue with automation criteria aligned to this codebase:

- **Deterministic pathing** with minimal random/world-state variance.
- **Low branching dialogue complexity**.
- **Low combat and low death risk**.
- **Clear item prerequisite gates** suitable for `WAIT_FOR_ITEMS`-style states.
- **Limited instanced/phased behavior** to reduce breakage.

## 4) Five new quest plugins to build next

These are five **new** candidates (excluding Family Pest and Cook's Assistant) that best match the current architecture.

### 1. Sheep Shearer

Why it fits:
- Single-NPC hand-in pattern and straightforward completion logic.
- Item gate is simple and deterministic (`20 x Balls of wool`).

Suggested state flow:
- `IDLE -> CHECK_WOOL -> TRAVEL_TO_FRED -> TALK_TO_FRED -> COMPLETE`.

### 2. Doric's Quest

Why it fits:
- Pure item hand-in quest with one main interaction loop.
- Clean requirement validation (`6 clay`, `4 copper ore`, `2 iron ore`) before travel.

Suggested state flow:
- `IDLE -> CHECK_ORES -> TRAVEL_TO_DORIC -> TALK_TO_DORIC -> COMPLETE`.

### 3. Witch's Potion

Why it fits:
- Short deterministic sequence with very limited routing.
- Clear item checks and a small fixed interaction set.

Suggested state flow:
- `IDLE -> CHECK_ITEMS -> TRAVEL_TO_HETTY -> TALK_TO_HETTY -> USE_ITEMS_IF_NEEDED -> COMPLETE`.

### 4. Imp Catcher

Why it fits:
- Straightforward hand-in once beads are prepared.
- Minimal dialogue branching and localized NPC interaction.

Suggested state flow:
- `IDLE -> CHECK_BEADS -> TRAVEL_TO_WIZARD_MIZGOG -> TALK_TO_WIZARD -> COMPLETE`.

### 5. Rune Mysteries

Why it fits:
- Beginner-friendly with explicit NPC hand-off chain.
- Multi-NPC routing is still deterministic and similar to Family Pest's style.

Suggested state flow:
- `IDLE -> TALK_TO_DUKE -> TRAVEL_TO_AUBURY -> TALK_TO_AUBURY -> RETURN_TO_DUKE -> COMPLETE`.

## 5) Build order recommendation

Recommended implementation order (lowest complexity to highest):

1. Sheep Shearer
2. Doric's Quest
3. Imp Catcher
4. Witch's Potion
5. Rune Mysteries

This order starts with single-NPC hand-ins and gradually introduces multi-step routing while staying inside established VitaLite plugin patterns.
