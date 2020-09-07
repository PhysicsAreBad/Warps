package me.physicsarebad.warps;

import me.physicsarebad.warps.commands.WarpCommand;
import me.physicsarebad.warps.guis.MainGUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Warps extends JavaPlugin {
    private static Warps instance;

    private FileConfiguration messages;

    private MainGUI mainGUI;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        File messageFile = new File(getDataFolder()+System.getProperty("file.separator")+"messages.yml");
        if (messageFile.exists()) {
            messages = YamlConfiguration.loadConfiguration(messageFile);
        } else {
            messages = YamlConfiguration.loadConfiguration(getTextResource("messages.yml"));
        }

        mainGUI = new MainGUI();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(mainGUI, this);

        getCommand("warps").setExecutor(new WarpCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Warps getInstance() {
        return instance;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public MainGUI getMainGUI() {
        return mainGUI;
    }
}
