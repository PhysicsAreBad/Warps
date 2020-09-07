package me.physicsarebad.warps.Utils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemCrafter {
    public static ItemStack getItem(Material mat, String name) {
        return getItem(mat, name, null);
    }

    public static ItemStack getItem(Material mat, String name, List<String> lore) {
        return getItem(mat, 1, name, lore);
    }

    public static ItemStack getItem(Material mat, int amount, String name, List<String> lore) {
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (lore != null) {
            im.setLore(lore);
        }
        is.setItemMeta(im);
        return is;
    }
}
