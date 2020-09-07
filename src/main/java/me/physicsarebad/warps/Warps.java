package me.physicsarebad.warps;

import me.physicsarebad.warps.commands.WarpCommand;
import me.physicsarebad.warps.guis.MainGUI;
import me.physicsarebad.warps.guis.WarpMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class Warps extends JavaPlugin {
    private static Warps instance;

    private FileConfiguration messages;

    private MainGUI mainGUI;
    private HashMap<MainGUI.WarpType, WarpMenu> menus = new HashMap<>();

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
        for (MainGUI.WarpType type : MainGUI.WarpType.values()) {
            menus.put(type, new WarpMenu(type));
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(mainGUI, this);
        for (MainGUI.WarpType type : MainGUI.WarpType.values()) {
            pm.registerEvents(menus.get(type), this);
        }

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

    public WarpMenu getMenu(MainGUI.WarpType type) {
        return menus.get(type);
    }
}
