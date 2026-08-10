# ExtraSounds Next - Changelog

## [1.5.7]
**Fixes**
- Fixed Tab key command autocompletion on Minecraft 26.1/26.2: the suggestion-window mixin still targeted the removed `keyPressed(int, int, int)` signature (26.1+ input uses `KeyEvent`), which broke suggestion display; holding Tab in chat repeatedly re-triggered the failure and caused a noticeable FPS drop

## [1.5.6]
**New**
- Minecraft 26.1/26.2 support (Fabric & NeoForge): mixins adapted for the new input/effect/screen systems, sound-pack generation deferred until item components are bound (sound manager is re-prepared afterwards), JEI integration entrypoint, NeoForge startup timing
- Trial Chambers content: the mace gets a dedicated heavy sound, spears are classified by their material, and copper tools/spears use the iron gear sound
- Dropping items in the world (Q key outside an inventory) now plays the drop sound

**Fixes**
- Fixed bows being classified with the bowl sound instead of the generic gear sound
- Fixed a startup crash on some NeoForge setups where the Minecraft mod container could not be resolved
