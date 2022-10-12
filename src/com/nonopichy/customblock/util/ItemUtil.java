package com.nonopichy.customblock.util;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    public static ItemStack createItemStack(Material material, int count, int model) {
        if (model == 0) {
            return new ItemStack(material, count);
        }

        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(new ItemStack(Material.ITEM_FRAME, count));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInt("CustomModelData", model);
        itemStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(itemStack);
    }
}
