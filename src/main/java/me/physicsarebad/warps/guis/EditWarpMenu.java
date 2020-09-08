package me.physicsarebad.warps.guis;

import me.physicsarebad.warps.storage.Warp;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class EditWarpMenu implements Listener {
    private HashMap<HumanEntity, Inventory> inventoryMap = new HashMap<>();
    private HashMap<HumanEntity, Warp> warpMap = new HashMap<>();


    public EditWarpMenu () {

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

    }
}
