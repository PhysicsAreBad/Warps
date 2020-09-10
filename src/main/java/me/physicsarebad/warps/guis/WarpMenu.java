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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;

public class WarpMenu implements Listener {

    private final MainGUI.WarpType type;

    private final HashMap<HumanEntity, Integer> pageMap = new HashMap<>();
    private final HashMap<HumanEntity, Inventory> inventoryMap = new HashMap<>();

    private final HashMap<HumanEntity, Warp> passwordMap = new HashMap<>();

    private List<Warp> warpList;

    public WarpMenu(MainGUI.WarpType warpType) {
        type = warpType;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Warps.getInstance(),
                () -> warpList = Warps.getInstance().getWarps(type),
                0,
                Warps.getInstance().getConfig().getInt("warp-update-speed")*20);
    }

    public void openInventory(HumanEntity e) {
        pageMap.put(e, 0);

        Inventory inv;

        if (inventoryMap.containsKey(e)) {
            inv = inventoryMap.get(e);
        } else {
            inv = Bukkit.createInventory(null, 54, type.name+"Warps");
        }
        inv.clear();

        inv.setItem(51, ItemCrafter.getItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"< Last Page"));
        inv.setItem(52, ItemCrafter.getItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Next Page >"));
        inv.setItem(53, ItemCrafter.getItem(Material.BARRIER, ChatColor.RED+""+ChatColor.BOLD+"Back"));

        for (int i = 0; i < 51; i++) {
            inv.setItem(i, warpList.get(i).getDisplayItem((Player) e));
        }

        for (int i = 0; i < 51; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, ItemCrafter.getItem(Material.GRAY_STAINED_GLASS_PANE, " "));
            }
        }

        inventoryMap.put(e, inv);

        e.openInventory(inv);
    }

    public void updateInventory (HumanEntity e, int page) {
        pageMap.put(e, page);

        Inventory inv = inventoryMap.get(e);

        inv.clear();

        inv.setItem(51, ItemCrafter.getItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"< Last Page"));
        inv.setItem(52, ItemCrafter.getItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Next Page >"));
        inv.setItem(53, ItemCrafter.getItem(Material.BARRIER, ChatColor.RED+""+ChatColor.BOLD+"Back"));

        for (int i = 51*page; i <51*(page+1); i++) {
            inv.addItem(warpList.get(i).getDisplayItem((Player) e));
        }

        for (int i = 0; i < 51; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, ItemCrafter.getItem(Material.GRAY_STAINED_GLASS_PANE, " "));
            }
        }

        inventoryMap.put(e, inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(type.name+"Warps")) {
            e.setCancelled(true);

            switch (e.getRawSlot()) {
                case 51:
                    if (pageMap.get(e.getWhoClicked()) > 0) {
                        updateInventory(e.getWhoClicked(), pageMap.get(e)-1);
                    }
                    break;
                case 52:
                    if (!isPageLast(pageMap.get(e.getWhoClicked()), warpList.size())) {
                        updateInventory(e.getWhoClicked(), pageMap.get(e)+1);
                    }
                    break;
                case 53:
                    Warps.getInstance().getMainGUI().openInventory(e.getWhoClicked());
                    break;
                default:
                    if (inventoryMap.get(e.getWhoClicked()).getItem(e.getRawSlot()).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                        if (e.isLeftClick()) {
                            Warp warp = warpList.get(pageMap.get(e.getWhoClicked()) * 51 + e.getRawSlot());
                            if (warp.getPassword().equals(null)) {
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().teleport(warp.getWarpLocation());
                            } else {
                                passwordMap.put(e.getWhoClicked(), warp);
                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().sendMessage(MessageUtil.getMessage(MessageType.REQUEST_PASSWORD));
                            }
                        } else if (e.isRightClick() && e.isShiftClick()) {
                            Warp warp = warpList.get(pageMap.get(e.getWhoClicked()) * 51 + e.getRawSlot());
                            if (warp.getCreator().getUniqueId() == e.getWhoClicked().getUniqueId()) {
                                Warps.getInstance().getEditMenu().openInventory(e.getWhoClicked(), warp, false);
                            }
                        } else if (e.isRightClick()) {
                            Warp warp = warpList.get(pageMap.get(e.getWhoClicked()) * 51 + e.getRawSlot());
                            if (warp.getCreator().getUniqueId() == e.getWhoClicked().getUniqueId()) {
                                SQLiteController.delete(warp.getId(), Warps.getInstance().getDatabaseFile(), type);
                            }
                        }
                    }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(type.name+"Warps")) {
            pageMap.remove(e.getPlayer());
            inventoryMap.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (passwordMap.containsKey(e.getPlayer())) {

            Warp warp = passwordMap.get(e.getPlayer());
            passwordMap.remove(e.getPlayer());

            String password = e.getMessage().trim();
            if (password.equals(warp.getPassword())) {
                e.getPlayer().teleport(warp.getWarpLocation());
            } else {
                e.getPlayer().sendMessage(MessageUtil.getMessage(MessageType.INCORRECT_PASSWORD));
            }
        }
    }

    static boolean isPageLast(int currentPage, int maxValue) {
        maxValue--;
        if ((currentPage+1)*51 <= (maxValue + (51-(maxValue % 51)))) {
            return false;
        }
        return true;
    }

}
