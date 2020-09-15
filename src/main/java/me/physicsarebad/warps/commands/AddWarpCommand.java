package me.physicsarebad.warps.commands;

import me.physicsarebad.warps.Utils.messages.MessageType;
import me.physicsarebad.warps.Utils.messages.MessageUtil;
import me.physicsarebad.warps.Utils.permissions.PermissionUtil;
import me.physicsarebad.warps.Warps;
import me.physicsarebad.warps.guis.MainGUI;
import me.physicsarebad.warps.storage.SQLiteController;
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
            Player player = (Player) sender;
            if (SQLiteController.getPlayerWarps(player, Warps.getInstance().getDatabaseFile()) < PermissionUtil.getWarpAmount(player) || sender.hasPermission("warps.admin")) {
                int id = 0;
                if (Warps.getInstance().getWarps(MainGUI.WarpType.PUBLIC).size() != 0) {
                    id = Warps.getInstance().getWarps(MainGUI.WarpType.PUBLIC).get(Warps.getInstance().getWarps(MainGUI.WarpType.PUBLIC).size() - 1).getId() + 1;
                }
                Warp warp = new Warp(id,
                        player,
                        Material.BARRIER,
                        sender.getName() + "'s Warp",
                        player.getLocation(),
                        null,
                        false);

                Warps.getInstance().getEditMenu().openInventory((Player) sender, warp, true);
            } else {
                sender.sendMessage(MessageUtil.getMessage(MessageType.TOO_MANY_WARPS));
            }
        }
        return true;
    }
}
