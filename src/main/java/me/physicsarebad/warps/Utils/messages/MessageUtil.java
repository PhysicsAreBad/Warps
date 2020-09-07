package me.physicsarebad.warps.Utils.messages;

import me.physicsarebad.warps.Warps;
import org.bukkit.ChatColor;

public class MessageUtil {
    public static String getMessage(MessageType type) {
        String prefix = ChatColor.translateAlternateColorCodes('&', Warps.getInstance().getMessages().getString("prefix"));
        return prefix + ChatColor.translateAlternateColorCodes('&', Warps.getInstance().getMessages().getString(type.getPath()));
    }
}
