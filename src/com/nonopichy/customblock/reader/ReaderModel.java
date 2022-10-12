package com.nonopichy.customblock.reader;

import com.nonopichy.customblock.interfaces.CustomBlock;
import com.nonopichy.customblock.interfaces.CustomItemBlock;
import com.nonopichy.customblock.util.FileUtil;
import com.nonopichy.customblock.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class ReaderModel {

    // Transform .bbmodel to overrides custommodeldata list (Util!)

    public static void init() {
        try {
            File file = new File("plugins/CustomBlock/models");
            File models = new File("plugins/CustomBlock/item_frame.json");

            models.delete();
            models.createNewFile();

            FileUtil.write(models, "{", "\"parent\": \"minecraft:item/generated\",", "\"textures\": {", "\"layer0\": \"minecraft:item/item_frame\"", "},", "\"overrides\": [", "]", "}");

            JSONObject jsonModelData = new JSONObject(FileUtil.read(models));
            JSONArray array = new JSONArray();

            int customModelData = 1;
            Bukkit.getConsoleSender().sendMessage("[CustomBlock]");

            // Register Items

            for (File dir : file.listFiles()) {
                if (!dir.isDirectory()) continue;
                if (dir.getName().contains("item")) {
                    for (File listFile : dir.listFiles()) {
                        if (listFile.getName().contains(".bbmodel")) {

                            String name = listFile.getName().replaceAll(".bbmodel", "");

                            int finalCustomModelData1 = customModelData;

                            CustomItemBlock customItemBlock = new CustomItemBlock() {
                                @Override
                                public String getName() {
                                    return name;
                                }

                                @Override
                                public int getModelID() {
                                    return finalCustomModelData1;

                                }
                            };
                            CustomItemBlock.customItemBlocks.put(name, customItemBlock);

                            array.put(new JSONObject("{\"predicate\": {\"custom_model_data\":" + customModelData + "}, \"model\": \"custom_items/" + name + "\"}"));

                            Bukkit.getConsoleSender().sendMessage("[CustomBlock] §a[%] Item Registered §c'" + name + "' §awith id §6(" + customModelData + ")");
                            Bukkit.getConsoleSender().sendMessage("[CustomBlock]");

                            customModelData += 1;
                        }

                    }

                }

            }

            // Register Blocks & Items Blocks

            for (File dir : file.listFiles()) {
                if (!dir.isDirectory()) continue;
                if (dir.getName().contains("item")) continue;

                File config = new File(dir.getAbsoluteFile() + "/config.json");

                ItemStack drop = getDrop(config);
                if (drop != null) {
                    Bukkit.getConsoleSender().sendMessage("[CustomBlock] §a[%] Drop item found! §6(" + drop + ")");
                } else {
                    Bukkit.getConsoleSender().sendMessage("[CustomBlock] §c[%] Drop item not found!");
                }

                Material resistance = getResistance(config);
                if (resistance != null) {
                    Bukkit.getConsoleSender().sendMessage("[CustomBlock] §a[%] Resistance material found! §6(" + resistance + ")");
                } else {
                    Bukkit.getConsoleSender().sendMessage("[CustomBlock] §c[%] Resistance material item not found!");
                    resistance = Material.SPAWNER;
                }

                for (File listFile : dir.listFiles()) {

                    if (listFile.getName().contains(".bbmodel")) {

                        String name = listFile.getName().replaceAll(".bbmodel", "");
                        if (name.contains("item")) continue;

                        int finalCustomModelData = customModelData;

                        array.put(new JSONObject("{\"predicate\": {\"custom_model_data\":" + customModelData + "}, \"model\": \"custom_blocks/" + name + "\"}"));

                        File item = new File(listFile.getAbsoluteFile().toString().replaceAll(listFile.getName(), "") + name + "_item.bbmodel");

                        // If zero, "not register" else register
                        int itemModel = 0;

                        // If block has item block, register
                        if (item.exists()) {
                            customModelData += 1;
                            itemModel = customModelData; // Register
                            array.put(new JSONObject("{\"predicate\": {\"custom_model_data\":" + customModelData + "}, \"model\": \"custom_blocks/" + name + "_item" + "\"}"));
                            Bukkit.getConsoleSender().sendMessage("[CustomBlock] §a[%] Item Block registered to §c'" + name + "_item' §awith id §6(" + customModelData + ")");
                        }

                        int finalItemModel = itemModel;

                        CustomItemBlock customItemBlock = new CustomItemBlock() {
                            @Override
                            public String getName() {
                                return name + "_item";
                            }

                            @Override
                            public int getModelID() {
                                return finalItemModel;
                            }
                        };
                        CustomItemBlock.customItemBlocks.put(name + "_item", customItemBlock);

                        Material finalResistance = resistance;
                        CustomBlock.register(new CustomBlock() {

                            @Override
                            public CustomItemBlock getItemBlock() {
                                return customItemBlock;
                            }

                            @Override
                            public String getName() {
                                return name;
                            }

                            @Override
                            public int getModel() {
                                return finalCustomModelData;
                            }

                            @Override
                            public Material getResistance() {
                                return finalResistance;
                            }

                            @Override
                            public ItemStack getDrop() {
                                return drop;
                            }
                        });

                        Bukkit.getConsoleSender().sendMessage("[CustomBlock] §a[%] Block Registered §c'" + name + "' §awith id §6(" + customModelData + ")");
                        Bukkit.getConsoleSender().sendMessage("[CustomBlock]");
                        customModelData += 1;

                    }

                }

            }

            // Saving all!

            jsonModelData.put("overrides", array);

            FileUtil.write(new File("plugins/CustomBlock/item_frame.json"), jsonModelData.toString());

            Bukkit.getConsoleSender().sendMessage("[CustomBlock] §a[%] Success finish! Blocks (" + CustomBlock.customBlocks.size() + ") Items (" + CustomItemBlock.customItemBlocks.size() + ")");
            Bukkit.getConsoleSender().sendMessage("[CustomBlock]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Material getResistance(File file) {
        try {
            JSONObject root = new JSONObject(FileUtil.read(file));
            return Material.valueOf(root.getString("resistance").toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack getDrop(File file) {
        try {
            JSONObject root = new JSONObject(FileUtil.read(file));
            JSONObject drop = root.getJSONObject("drop");

            String rawMaterial = drop.getString("material");
            int amount = drop.getInt("amount");

            if (rawMaterial.contains("model-")) {
                String model = rawMaterial.split("model-")[1];

                for (CustomItemBlock value : CustomItemBlock.customItemBlocks.values()) {
                    if (model.equalsIgnoreCase(value.getName())) {
                        return value.getItemStack(amount);
                    }
                }
            }

            Material material = Material.valueOf(rawMaterial.toUpperCase());
            int model = drop.getInt("model");

            return ItemUtil.createItemStack(material, amount, model);
        } catch (Exception e) {
            return null;
        }
    }
}