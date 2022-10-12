package com.nonopichy.customblock.entity;

import com.nonopichy.customblock.interfaces.CustomBlock;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static com.nonopichy.customblock.interfaces.CustomBlock.customBlocks;

public class CustomItemFrame extends EntityItemFrame{

    public CustomItemFrame(EntityTypes<? extends EntityItemFrame> entitytypes, World world) {
        super(entitytypes, world);
    }

    public CustomItemFrame(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        super(world, blockposition, enumdirection);
    }

    public int tickBarrier = 1;

    @Override
    public void tick() {
        super.tick();
        Material material = world.getType(blockPosition).getMaterial();
        if(material.equals(Material.AIR))
            end();
        else {
            Location location = new Location(Bukkit.getWorld(world.getWorld().getName()), blockPosition.getX(),blockPosition.getY(),blockPosition.getZ());
            if(location.getBlock().getType() == org.bukkit.Material.SPAWNER){
                tickBarrier++;
                if(tickBarrier>=100){
                    tickBarrier = 1;
                    location.getBlock().setType(org.bukkit.Material.BARRIER);
                }
            }
        }
    }

    private void end(){
        ItemStack itemStack = getItem();
        int model = itemStack.getTag().getInt("CustomModelData");

        // Checking model with all customblocks to drop

        if(model != 0){
            for (CustomBlock value : customBlocks.values()) {
                if(value.getModel() == model){
                        Location loc = new Location(Bukkit.getWorld(world.getWorld().getName()),blockPosition.getX(),blockPosition.getY(),blockPosition.getZ());
                        world.getWorld().dropItem(loc,
                                (value.getDrop() == null) ? value.getItemBlock().getItemStack(1) : value.getDrop());
                    break;
                }
            }
        }

        killEntity();

    }
    public static void createCustomItemFrame(Location loc, int model){
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

        CustomItemFrame customItemFrame = new CustomItemFrame(world,
                new BlockPosition(loc.getX(),loc.getY(),loc.getZ()), EnumDirection.UP);
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.ITEM_FRAME));

        NBTTagCompound customTag = new NBTTagCompound();
        customTag.setInt("CustomModelData",model);

        itemStack.setTag(customTag);

        customItemFrame.setItem(itemStack);
        customItemFrame.setInvulnerable(true);
        customItemFrame.setInvisible(true);

        customItemFrame.fixed = true;
        customItemFrame.itemDropChance = 0f;
        customItemFrame.setDirection(EnumDirection.UP);
        customItemFrame.setSilent(true);

        world.addEntity(customItemFrame, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }


}