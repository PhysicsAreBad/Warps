package me.physicsarebad.warps.commands;

import me.physicsarebad.warps.Warps;
import me.physicsarebad.warps.guis.MainGUI;
import me.physicsarebad.warps.storage.Warp;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Warp warp = new Warp(Warps.getInstance().getWarps(MainGUI.WarpType.PUBLIC).get(Warps.getInstance().getWarps(MainGUI.WarpType.PUBLIC).size() - 1).getId() + 1,
                    ((Player) sender),
                    Material.BARRIER,
                    sender.getName()+"'s Warp",
                    ((Player) sender).getLocation(),
                    null,
                    false);

            Warps.getInstance().getEditMenu().openInventory((Player) sender, warp, true);
        }
        return true;
    }
}
