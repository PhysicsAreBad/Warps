package me.physicsarebad.warps.Utils.messages;

public enum MessageType {
    NO_PERMISSION("no-permission"),
    NOT_PLAYER("must-be-player"),
    INCORRECT_PASSWORD("wrong-password");

    private String path;

    MessageType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
