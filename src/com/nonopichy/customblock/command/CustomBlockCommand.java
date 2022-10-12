package com.nonopichy.customblock.command;

import com.nonopichy.customblock.interfaces.CustomBlock;
import com.nonopichy.customblock.interfaces.CustomItemBlock;
import com.nonopichy.customblock.reader.ReaderModel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nonopichy.customblock.interfaces.CustomBlock.customBlocks;

public class CustomBlockCommand implements CommandExecutor, TabCompleter {

    public static Location pos1 = null;
    public static Location pos2 = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p =  ((Player) sender);

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("reload")) {
                customBlocks.clear();
                ReaderModel.init();
                p.sendMessage(ChatColor.GREEN + "Maybe success reload! (Check console)!");
                return true;
            }
            /*
            if(args[0].equalsIgnoreCase("pos1")) {
                pos1 = p.getLocation();
                p.sendMessage("Pos1");
                return true;
            }
            if(args[0].equalsIgnoreCase("pos2")) {
                pos2 = p.getLocation();
                p.sendMessage("Pos2");
                return true;
            }
            if(args[0].equalsIgnoreCase("block")) {
                CustomBlock rainbow = customBlocks.get("rainbow_block");
                for (Location location : getSquare(pos1, pos2)) {
                    rainbow.setBlock(location);
                }
                p.sendMessage("setting");
                return true;
            }
             */
        }

        if(args.length > 1){
            try {
                if(args[0].equalsIgnoreCase("item")) {
                    for (CustomItemBlock value : CustomItemBlock.customItemBlocks.values()) {
                        if (value.getName().equalsIgnoreCase(args[1])) {
                            p.getInventory().addItem(value.getItemStack(1));
                            p.sendMessage("Giving item... " + value.getName());
                            return true;
                        }
                    }
                }
                for (CustomBlock value : customBlocks.values()) {
                    if (args[0].equalsIgnoreCase("item") && value.getItemBlock().getName().equalsIgnoreCase(args[1])) {
                        p.getInventory().addItem(value.getItemBlock().getItemStack(1));
                        p.sendMessage("Giving item... " + value.getItemBlock().getName());
                        return true;
                    }
                    else if (value.getName().equalsIgnoreCase(args[1])) {
                        value.setBlock(p.getLocation());
                        p.sendMessage("Setting block... " + value.getName());
                        return true;
                    }
                }
            } catch (Exception e){
                p.sendMessage(ChatColor.RED+"Information input invalid!");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length < 2)
            return Arrays.asList("block","item","reload");
        if(args[0].equalsIgnoreCase("block"))
            return new ArrayList<>(customBlocks.keySet());
        return new ArrayList<>(CustomItemBlock.customItemBlocks.keySet());

    }




    public static List<Location> getSquare(final Location location1, final Location location2){
        List<Location> list = new ArrayList<>();
        final World world = location1.getWorld();

        int highestX = Math.max(location2.getBlockX(), location1.getBlockX());
        int lowestX = Math.min(location2.getBlockX(), location1.getBlockX());

        int highestY = Math.max(location2.getBlockY(), location1.getBlockY());
        int lowestY = Math.min(location2.getBlockY(), location1.getBlockY());

        int highestZ = Math.max(location2.getBlockZ(), location1.getBlockZ());
        int lowestZ = Math.min(location2.getBlockZ(), location1.getBlockZ());

        for(int x = lowestX; x <= highestX; x++)
            for(int z = lowestZ; z <= highestZ; z++)
                for(int y = lowestY; y <= highestY; y++)
                    list.add(new Location(world, x, y, z));

        return list;
    }

}
