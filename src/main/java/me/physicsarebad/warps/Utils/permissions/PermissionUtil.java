package me.physicsarebad.warps.Utils.permissions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionUtil {
    public static int getWarpAmount(Player player) {
        String permissionPrefix = "warp.amount.";

        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            String permission = attachmentInfo.getPermission();
            if (permission.startsWith(permissionPrefix)) {
                try {
                    return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
                } catch (NumberFormatException e) {
                    return Integer.MAX_VALUE; //Wildcard value
                }
            }
        }

        return 0; //Default 0
    }
}
