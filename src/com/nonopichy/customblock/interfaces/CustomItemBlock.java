package com.nonopichy.customblock.interfaces;

import com.nonopichy.customblock.util.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface CustomItemBlock {

    HashMap<String, CustomItemBlock> customItemBlocks = new HashMap<>();

    String getName();
    int getModelID();
    default ItemStack getItemStack(int count){
        if(getModelID() == 0)
            return null;
        return ItemUtil.createItemStack(null,count, getModelID());
    }
}
