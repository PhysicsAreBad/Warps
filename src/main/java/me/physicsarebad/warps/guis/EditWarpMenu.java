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

        dataMap.put(entity, new PlayerData(inv, warp, isNew, MainGUI.WarpType.PUBLIC));

        updateExampleItem(inv, warp);

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
                case 12:
                    toggleName.add(e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.TOGGLE_NAME));
                    break;
                case 14:
                    data.warp.setGlow(!data.warp.getGlow());
                    break;
                case 16:
                    togglePassword.add(e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.TOGGLE_PASSWORD));
                    break;
                case 25:
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
            }

            updateExampleItem(data.inv, data.warp);
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
            dataMap.get(e.getPlayer()).warp.setPassword(e.getMessage().trim());
            e.getPlayer().openInventory(dataMap.get(e.getPlayer()).inv);
            togglePassword.remove(e.getPlayer());
        } else if (toggleName.contains(e.getPlayer())) {
            dataMap.get(e.getPlayer()).warp.setName(e.getMessage().trim());
            e.getPlayer().openInventory(dataMap.get(e.getPlayer()).inv);
            toggleName.remove(e.getPlayer());
        }
    }

    private void updateExampleItem(Inventory inv, Warp warp) {
        ItemStack is = warp.getDisplayItem(warp.getCreator().getPlayer());
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE+"This is what your warp will look like!");
        im.setLore(lore);
        inv.setItem(13, is);
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
