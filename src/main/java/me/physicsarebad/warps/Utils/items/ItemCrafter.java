package me.physicsarebad.warps.Utils.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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
        return getItem(mat, amount, name, lore, false);
    }

    public static ItemStack getItem(Material mat, int amount, String name, List<String> lore, boolean glow) {
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glow) {
            im.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (lore != null) {
            im.setLore(lore);
        }
        is.setItemMeta(im);
        return is;
    }
}
