package me.physicsarebad.warps.Utils.messages;

public enum MessageType {
    NO_PERMISSION("no-permission"),
    TOO_MANY_WARPS("too-many-warps"),
    NOT_PLAYER("must-be-player"),
    REQUEST_PASSWORD("send-password"),
    INCORRECT_PASSWORD("wrong-password"),
    TOGGLE_NAME("toggle-name"),
    TOGGLE_PASSWORD("toggle-password"),
    NEED_PASSWORD("need-password"),
    CREATED_WARP("created-warp"),
    TELEPORT("teleport");

    private String path;

    MessageType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
