package me.physicsarebad.warps;

import me.physicsarebad.warps.commands.AddWarpCommand;
import me.physicsarebad.warps.commands.WarpCommand;
import me.physicsarebad.warps.guis.EditWarpMenu;
import me.physicsarebad.warps.guis.MainGUI;
import me.physicsarebad.warps.guis.WarpMenu;
import me.physicsarebad.warps.storage.SQLiteController;
import me.physicsarebad.warps.storage.Warp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public final class Warps extends JavaPlugin {
    private static Warps instance;

    private FileConfiguration messages;
    private File databaseFile = new File(getDataFolder() +System.getProperty("file.separator")+"warps.db");

    private MainGUI mainGUI;
    private HashMap<MainGUI.WarpType, WarpMenu> menus = new HashMap<>();
    private EditWarpMenu editWarpMenu;

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

        if (!databaseFile.exists()) {
            SQLiteController.init(databaseFile);
        }

        mainGUI = new MainGUI();
        editWarpMenu = new EditWarpMenu();
        for (MainGUI.WarpType type : MainGUI.WarpType.values()) {
            menus.put(type, new WarpMenu(type));
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(mainGUI, this);
        for (MainGUI.WarpType type : MainGUI.WarpType.values()) {
            pm.registerEvents(menus.get(type), this);
        }

        getCommand("warps").setExecutor(new WarpCommand());
        getCommand("addwarp").setExecutor(new AddWarpCommand());
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

    public List<Warp> getWarps(MainGUI.WarpType type) {
        return SQLiteController.getWarps(type, databaseFile);
    }

    public EditWarpMenu getEditMenu() {
        return editWarpMenu;
    }

    public File getDatabaseFile() {
        return databaseFile;
    }
}
