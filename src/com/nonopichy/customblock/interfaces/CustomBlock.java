package com.nonopichy.customblock.interfaces;

import com.nonopichy.customblock.entity.CustomItemFrame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface CustomBlock {

    HashMap<String, CustomBlock> customBlocks = new HashMap<>();

    CustomItemBlock getItemBlock();

    String getName();
    int getModel();
    Material getResistance();
    ItemStack getDrop();

    default void setBlock(Location loc){
        // Define model to location
        CustomItemFrame.createCustomItemFrame(loc, getModel());
        // Otimize Spawner Block
        Block b = loc.getBlock();
        b.setType(Material.BARRIER);
        /*
        CreatureSpawner SpawnerCreature = (CreatureSpawner)b.getState();
        SpawnerCreature.setDelay(-1);
        SpawnerCreature.setSpawnedType(EntityType.AREA_EFFECT_CLOUD);
        SpawnerCreature.update();

         */
    }

    static void register(CustomBlock customBlock) {
        customBlocks.put(customBlock.getName(), customBlock);
    }
}
