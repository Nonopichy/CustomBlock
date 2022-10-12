package com.nonopichy.customblock;

import com.nonopichy.customblock.command.CustomBlockCommand;
import com.nonopichy.customblock.listener.CustomBlockListener;
import com.nonopichy.customblock.reader.ReaderModel;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {

        getServer().getPluginManager().registerEvents(new CustomBlockListener(), this);

        CustomBlockCommand customBlockCommand = new CustomBlockCommand();
        getCommand("customblock").setExecutor(customBlockCommand);
        getCommand("customblock").setTabCompleter(customBlockCommand);
        ReaderModel.init();
    }

}
