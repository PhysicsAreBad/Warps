package me.physicsarebad.warps.Utils.messages;

public enum MessageType {
    NO_PERMISSION("no-permission"),
    NOT_PLAYER("must-be-player"),
    REQUEST_PASSWORD("send-password"),
    INCORRECT_PASSWORD("wrong-password"),
    TOGGLE_NAME("toggle-name"),
    TOGGLE_PASSWORD("toggle-password"),
    CREATED_WARP("created-warp");

    private String path;

    MessageType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
