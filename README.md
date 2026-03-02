# Family Pest Miniquest Plugin

A VitaLite external plugin that automates the Family Pest miniquest in Old School RuneScape.

## Requirements

- **Quest Prerequisite:** Family Crest (completed)
- **Items Required:** 500,000 coins
- **Recommended Teleports:** 
  - Varrock Teleport (for Dimintheis)
  - Camelot/Catherby Teleport (for Caleb)
  - Ring of Dueling (for Avan)
  - Lumberyard Teleport (for Johnathon)

## Features

- Automatically detects quest progress and resumes from where you left off
- Handles dialogue with all brothers (Dimintheis, Avan, Caleb, Johnathon)
- Walker pathfinding handles doors automatically
- Supports multiple teleport tablet types
- Displays current state on overlay

## Installation

1. Build the plugin: `gradlew jar`
2. Copy `build/libs/familypest-1.0.0.jar` to your RuneLite sideloaded-plugins folder
3. Enable the plugin in RuneLite

## Usage

1. Ensure you have completed Family Crest quest
2. Have 500k coins in inventory
3. Start the plugin
4. The plugin will automatically visit all brothers and complete the miniquest

## Reward

- Ability to own all three Steel Gauntlet variants (Chaos, Cooking, Goldsmith) simultaneously
- Free replacement gauntlets from Dimintheis or any brother

## License

MIT