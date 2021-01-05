package de.z1up.supernick.nick;

import java.util.UUID;

public class NickPlayer {

    private UUID uuid;
    private boolean nicked;
    private boolean autoNick;
    private UUID nickedAs;

    public NickPlayer(UUID uuid, boolean nicked, boolean autoNick, UUID nickedAs) {
        this.uuid = uuid;
        this.nicked = nicked;
        this.autoNick = autoNick;
        this.nickedAs = nickedAs;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isNicked() {
        return nicked;
    }

    public boolean usesAutoNick() {
        return autoNick;
    }

    public UUID getNickedAs() {
        return nickedAs;
    }

    public void insert() {
        NickManager.instance.getNickWrapper().insertPlayer(this);
    }

    public void update() {
        NickManager.instance.getNickWrapper().updatePlayer(this);
    }

    public void updateNickName() {
        NickManager.instance.getNickWrapper().updateNickName(this);
    }

    public void setNicked(boolean nicked) {
        this.nicked = nicked;
    }

    public void setAutoNick(boolean autoNick) {
        this.autoNick = autoNick;
    }

    public void setNickedAs(UUID nickedAs) {
        this.nickedAs = nickedAs;
    }
}
