package me.physicsarebad.warps.guis;

import me.physicsarebad.warps.Utils.items.ItemCrafter;
import me.physicsarebad.warps.Utils.messages.MessageType;
import me.physicsarebad.warps.Utils.messages.MessageUtil;
import me.physicsarebad.warps.Warps;
import me.physicsarebad.warps.storage.SQLiteController;
import me.physicsarebad.warps.storage.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditWarpMenu implements Listener {
    private HashMap<HumanEntity, PlayerData> dataMap = new HashMap<>();
    private List<HumanEntity> togglePassword = new ArrayList<>();
    private List<HumanEntity> toggleName = new ArrayList<>();

    public void openInventory(HumanEntity entity, Warp warp, boolean isNew) {
        Inventory inv = Bukkit.createInventory(null, 27, "Edit Warp");

        PlayerData data = new PlayerData(inv, warp, isNew, MainGUI.WarpType.PUBLIC);
        dataMap.put(entity, data);

        updateExampleItem(data);
        updateWarpType(data);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Click on any material in your inventory to change the icon.");
        inv.setItem(10, ItemCrafter.getItem(warp.getMaterial(), ChatColor.BLUE+""+ChatColor.BOLD+"Item Material", lore));

        inv.setItem(12, ItemCrafter.getItem(Material.OAK_SIGN, ChatColor.GRAY+""+ChatColor.BOLD+"Set Name"));

        inv.setItem(14, ItemCrafter.getItem(Material.ENCHANTED_BOOK, ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"Toggle Glow"));

        inv.setItem(16, ItemCrafter.getItem(Material.PAPER, ChatColor.GOLD+""+ChatColor.BOLD+"Set Password"));

        inv.setItem(25, ItemCrafter.getItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN+""+ChatColor.BOLD+"Create"));
        inv.setItem(26, ItemCrafter.getItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED+""+ChatColor.BOLD+"Exit"));

        for (int i = 0; i < 27; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, ItemCrafter.getItem(Material.GRAY_STAINED_GLASS_PANE, " "));
            }
        }

        entity.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Edit Warp")) {
            e.setCancelled(true);
            PlayerData data = dataMap.get(e.getWhoClicked());
            switch (e.getRawSlot()) {
                case 4:
                    int id = data.warpType.id+1;
                    if (id > 2) {
                        id = 0;
                    }

                    data.warpType = MainGUI.WarpType.values()[id];

                    if (data.warpType != MainGUI.WarpType.PRIVATE) {
                        data.warp.setPassword(null);
                    }

                    updateWarpType(data);
                    break;
                case 12:
                    toggleName.add(e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.TOGGLE_NAME));
                    break;
                case 14:
                    data.warp.setGlow(!data.warp.getGlow());
                    break;
                case 16:
                    if (data.warpType == MainGUI.WarpType.PRIVATE) {
                        togglePassword.add(e.getWhoClicked());
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.TOGGLE_PASSWORD));
                    }
                    break;
                case 25:
                    if (data.warpType == MainGUI.WarpType.PRIVATE && data.warp.getPassword() == null) {
                        e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.NEED_PASSWORD));
                        return;
                    }
                    e.getWhoClicked().closeInventory();
                    if (data.isNew) {
                        SQLiteController.addWarp(data.warpType, data.warp, Warps.getInstance().getDatabaseFile());
                    } else {
                        if (data.warpType == data.startWarpType) {
                            SQLiteController.update(data.warpType, data.warp, data.warp.getId(), Warps.getInstance().getDatabaseFile());
                        } else {
                            SQLiteController.delete(data.warp.getId(), Warps.getInstance().getDatabaseFile(), data.startWarpType);
                            SQLiteController.addWarp(data.warpType, data.warp, Warps.getInstance().getDatabaseFile());
                        }
                    }
                    e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.CREATED_WARP));
                    break;
                case 26:
                    e.getWhoClicked().closeInventory();
                    break;
                default:
                    if (e.getRawSlot() > 26 && e.getCurrentItem() != null) {
                        data.warp.setMaterial(e.getCurrentItem().getType());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.YELLOW+"Click on any material in your inventory to change the icon.");
                        data.inv.setItem(10, ItemCrafter.getItem(data.warp.getMaterial(), ChatColor.BLUE+""+ChatColor.BOLD+"Item Material", lore));
                    }
                    break;
            }

            updateExampleItem(data);
        }
    }

    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (!togglePassword.contains(e.getPlayer()) && !toggleName.contains(e.getPlayer())) {
            dataMap.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onChat (AsyncPlayerChatEvent e) {
        if (togglePassword.contains(e.getPlayer())) {
            e.setCancelled(true);
            dataMap.get(e.getPlayer()).warp.setPassword(e.getMessage().trim());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Warps.getInstance(), () -> e.getPlayer().openInventory(dataMap.get(e.getPlayer()).inv), 5);
            togglePassword.remove(e.getPlayer());
            updateExampleItem(dataMap.get(e.getPlayer()));
        } else if (toggleName.contains(e.getPlayer())) {
            e.setCancelled(true);
            dataMap.get(e.getPlayer()).warp.setName(e.getMessage().trim());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Warps.getInstance(), () -> e.getPlayer().openInventory(dataMap.get(e.getPlayer()).inv), 5);
            toggleName.remove(e.getPlayer());
            updateExampleItem(dataMap.get(e.getPlayer()));
        }
    }

    private void updateExampleItem(PlayerData data) {
        ItemStack is = data.warp.getDisplayItem(data.warp.getCreator().getPlayer());
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE+"This is what your warp will look like!");
        lore.add("Current Name " + data.warp.getName());
        if (data.warp.getPassword() != null)
        im.setLore(lore);
        data.inv.setItem(13, is);
    }

    private void updateWarpType(PlayerData data) {
        List<String> lore = new ArrayList<>();

        switch (data.warpType) {
            case PUBLIC:
                lore.add(ChatColor.GREEN+"> Public Warp");
                lore.add(ChatColor.GRAY+"Private Warp");
                lore.add(ChatColor.GRAY+"Server Warp");
                break;
            case PRIVATE:
                lore.add(ChatColor.GRAY+"Public Warp");
                lore.add(ChatColor.RED+"> Private Warp");
                lore.add(ChatColor.GRAY+"Server Warp");
                break;
            case SERVER:
                lore.add(ChatColor.GRAY+"Public Warp");
                lore.add(ChatColor.GRAY+"Private Warp");
                lore.add(ChatColor.LIGHT_PURPLE+"> Server Warp");
                break;
        }

        data.inv.setItem(4, ItemCrafter.getItem(Material.HOPPER, ChatColor.WHITE+""+ChatColor.BOLD+"Select Type", lore));
    }
}

class PlayerData {
    Inventory inv;
    Warp warp;
    boolean isNew;
    MainGUI.WarpType warpType;
    MainGUI.WarpType startWarpType;

    public PlayerData(Inventory inv, Warp warp, boolean isNew, MainGUI.WarpType warpType) {
        this.inv = inv;
        this.warp = warp;
        this.isNew = isNew;
        this.warpType = warpType;
        startWarpType = warpType;
    }
}
