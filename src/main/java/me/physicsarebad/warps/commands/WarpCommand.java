package me.physicsarebad.warps.commands;

import me.physicsarebad.warps.Utils.messages.MessageType;
import me.physicsarebad.warps.Utils.messages.MessageUtil;
import me.physicsarebad.warps.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof HumanEntity) {
            Warps.getInstance().getMainGUI().openInventory((HumanEntity) sender);
        } else if (sender != null) {
            sender.sendMessage(MessageUtil.getMessage(MessageType.NOT_PLAYER));
        }
        return true;
    }
}
