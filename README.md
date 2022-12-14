[![download](https://img.shields.io/badge/download-jar-green)](https://github.com/Nonopichy/CustomBlock/releases/)
[![download-models](https://img.shields.io/badge/model-orange)](https://github.com/Nonopichy/CustomBlock/raw/main/models.rar)
[![resource-pack-download](https://img.shields.io/badge/resource%20pack-blue)](https://github.com/Nonopichy/CustomBlock/raw/main/resource%20pack.zip)

![customblock](https://user-images.githubusercontent.com/68911691/195412546-feadd07b-39e2-41a1-a9d7-b5e14bec3533.png)
# CustomBlock
-   Version 1.16.5
-   Inspiration [Cloud Wolf DataPack Custom Block](https://www.youtube.com/watch?v=ENK0b_2yT1c&ab_channel=CloudWolf)
-   Video experiencing [one](https://www.youtube.com/shorts/a4BgBC0Ps1o), [two](https://www.youtube.com/shorts/ng-WkJ6Y6kM)
## The problem
The current version of this plugin contains a serious error, accumulated entities, already in a more efficient solution so that it can run on any minecraft server, but however ignoring this problem, it works efficiently.
## Registering block
-   Create a new dir without contains `item` in `CustomBlock/models`
-   Example: `CustomBlocks/models/rainbow_block`
-   This dir place your `.bbmodel` of `block`
-   Example: `CustomBlocks/models/rainbow_block/rainbow_block.bbmodel`
## Registering item block
-   Place in `CustomBlocks/models/rainbow_block` a `.bbmodel` with the name of last `.bbmodel` pasting `_item` before point extesion.
-   Example: `CustomBlocks/models/rainbow_block/rainbow_block_item.bbmodel`
## Block configuration
-   In `CustomBlocks/models/rainbow_block` create a new file call `config.json`
-   Example: `CustomBlocks/models/rainbow_block/config.json`
-   Paste in `config.json` (Default, drop item block registred):
```json
{
 "drop": {
  "model": 0,
  "amount": 1
 }
}
```
-   Example for reference other item custom to drop with id:
```json
{
 "drop": {
  "model": 1,
  "amount": 1
 }
}
```
-   Example for reference other item custom to drop with material:
```json
{
 "drop": {
  "material": "model-<item_name_file_without_extesion>",
  "amount": 1
 }
}
```
-   Example for reference other any item:
```json
{
 "drop": {
  "material": "STONE",
  "amount": 1
 }
}
```
## Registering item
-   Create a new dir contains `item` in `CustomBlock/models`
-   Example: `CustomBlocks/models/rainbow_item`
-   This dir place your `.bbmodel` list
-   Example: `CustomBlocks/models/rainbow_item/rainbow.bbmodel`
## Resource pack
-   In minecraft input command `/customblock reload`
-   Copy `CustomBlocks/item_frame.json` to `assets/minecraf/model/item/item_frame.json` of your texture
-   Warn, you need convert your models to json minecraft and place in your texture (maybe on future this be possible auto)
-   Done
