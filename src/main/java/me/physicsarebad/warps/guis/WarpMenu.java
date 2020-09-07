package me.physicsarebad.warps.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class WarpMenu implements Listener {

    private Inventory inv;
    private MainGUI.WarpType type;

    public WarpMenu(MainGUI.WarpType warpType) {
        type = warpType;

        inv = Bukkit.createInventory(null, 54, type.name+"Warps");
    }

    public void openInventory(HumanEntity e) {
        e.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(type.name+"Warps")) {
            e.setCancelled(true);
        }
    }

}
