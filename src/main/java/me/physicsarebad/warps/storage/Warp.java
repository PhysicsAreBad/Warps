package me.physicsarebad.warps.storage;

import me.physicsarebad.warps.Utils.items.ItemCrafter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class Warp {
    private OfflinePlayer creator;
    private Material mat;
    private String name;
    private Location warpLoc;
    private String password;

    public Warp(OfflinePlayer creator, Material mat, String name, Location warpLoc, String password) {
        this.creator = creator;
        this.mat = mat;
        this.name = name;
        this.warpLoc = warpLoc;
    }

    public ItemStack getDisplayItem() {
        return ItemCrafter.getItem(mat, name);
    }

    public OfflinePlayer getCreator() {
        return creator;
    }

    public Location getWarpLocation() {
        return warpLoc;
    }

    public Material getMaterial() {
        return mat;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
