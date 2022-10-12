package com.nonopichy.customblock.listener;

import com.nonopichy.customblock.entity.CustomItemFrame;
import com.nonopichy.customblock.interfaces.CustomBlock;
import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_16_R3.EntityItemFrame;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItemFrame;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CustomBlockListener implements Listener {

    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoadEvent(ChunkLoadEvent e) {
        //  if(e.getChunk().isLoaded())
        //     return;

        if (new Random().nextBoolean())
            CustomBlock.customBlocks.get("rainbow_ore").setBlock(e.getChunk().getBlock(5, 60, 5).getLocation());
    }

     */
    // Very important, load offlines customblocks
    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoadEvent(ChunkLoadEvent e){

        for (Entity entity : e.getChunk().getEntities()) {
            if(entity instanceof ItemFrame){
                ItemFrame itemFrame = (ItemFrame) entity;

                net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(itemFrame.getItem());
                NBTTagCompound tag = item.getTag();
                Location loc = itemFrame.getLocation();

                if(tag != null){
                   int model = tag.getInt("CustomModelData");
                   if(model != 0){
                       CustomItemFrame.createCustomItemFrame(loc, model);
                       entity.remove();
                   }
                }
            }
        }

    }
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractBreak(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();

        if (b == null && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Location loc = b.getLocation();

        for (Entity entity : b.getWorld().getNearbyEntities(b.getLocation(), 2, 2, 2)) {
            if (compareLocation(loc, entity.getLocation().clone().subtract(1, 0, 1))) {
                if (entity instanceof ItemFrame && !isCustomItemFrame(entity)) changeToCustom(entity);
                changeResistance(b, Material.SPAWNER);
            }
        }
    }

    public int isCustomItem(@NotNull ItemStack itemStack) {
        if (itemStack.getType() != Material.ITEM_FRAME) return 0;
        NBTTagCompound tag = CraftItemStack.asNMSCopy(itemStack).getTag();
        return (tag != null) ? tag.getInt("CustomModelData") : 0;
    }

    // Place custom block
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack itemStack = p.getItemInHand().clone();
        Block b = e.getClickedBlock();
        int model = isCustomItem(itemStack);

        if (b == null || model == 0) return;

        for (CustomBlock value : CustomBlock.customBlocks.values()) {
            if (model != value.getItemBlock().getModelID()) continue;

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);

                Block blockRelative = e.getClickedBlock().getRelative(e.getBlockFace());

                if (p.getGameMode() != GameMode.CREATIVE) {
                    itemStack.setAmount(1);
                    p.getInventory().removeItem(itemStack);
                }

                value.setBlock(blockRelative.getLocation());
            }

            break;
        }

    }


    public boolean compareLocation(Location one, Location two) {
        return (int) one.getX() == (int) two.getX() && (int) one.getY() == (int) two.getY() && (int) one.getZ() == (int) two.getZ();
    }

    public void changeResistance(Block b, Material material) {
        if (b.getType() == Material.BARRIER) {
            b.setType(material);
            if (material == Material.SPAWNER) {
                CreatureSpawner SpawnerCreature = (CreatureSpawner) b.getState();
                SpawnerCreature.setDelay(-1);
                SpawnerCreature.setSpawnedType(EntityType.AREA_EFFECT_CLOUD);
                SpawnerCreature.update();
            }
        }
    }

    public boolean isCustomItemFrame(Entity entity) {
        if (!(entity instanceof ItemFrame)) return false;
        EntityItemFrame entityItemFrame = ((CraftItemFrame) entity).getHandle();
        return entityItemFrame.getClass().equals(CustomItemFrame.class);
    }

    public void changeToCustom(Entity entity) {
        ItemFrame itemFrame = (ItemFrame) entity;

        net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(itemFrame.getItem());
        NBTTagCompound tag = item.getTag();
        Location locItem = itemFrame.getLocation();

        if (tag != null) {
            int model = tag.getInt("CustomModelData");
            if (model != 0) {
                CustomItemFrame.createCustomItemFrame(locItem, model);
                entity.remove();
            }
        }
    }

}
