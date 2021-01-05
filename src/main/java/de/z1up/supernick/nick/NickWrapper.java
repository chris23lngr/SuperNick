package de.z1up.supernick.nick;

import de.z1up.supernick.mysql.SQL;
import de.z1up.supernick.util.UUIDFetcher;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class NickWrapper {

    private SQL                 sql;
    private final String        TABLE_NAME = "nick_players";
    private final String        ATTRIBUTE_UUID = "UUID";
    private final String        ATTRIBUTE_NICKED = "NICKED";
    private final String        ATTRIBUTE_AUTO_NICK = "AUTO_NICK";
    private final String        ATTRIBUTE_NICKED_AS = "NICKED_AS";

    public NickWrapper(SQL sql) {
        this.sql = sql;
        createTable();
    }

    public void createTable() {

        String stmt = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ATTRIBUTE_UUID + " varchar(256), " + ATTRIBUTE_NICKED + " varchar(8), " + ATTRIBUTE_AUTO_NICK + " varchar(8), " + ATTRIBUTE_NICKED_AS + " varchar(256))";
        sql.executeUpdateAsync(stmt, null);

    }

    public void insertPlayer(NickPlayer player) {

        UUID uuid = player.getUuid();
        String nicked = String.valueOf(player.isNicked());
        String autoNick = String.valueOf(player.usesAutoNick());
        UUID nickedAs = player.getNickedAs();

        String stmt = "INSERT INTO " + TABLE_NAME + "(" + ATTRIBUTE_UUID + ", " + ATTRIBUTE_NICKED + ", " + ATTRIBUTE_AUTO_NICK + ", " + ATTRIBUTE_NICKED_AS + ") VALUES (?, ?, ? ,?)";
        sql.executeUpdateAsync(stmt, Arrays.asList(uuid, nicked, autoNick, nickedAs));

    }

    public void updatePlayer(NickPlayer player) {

        UUID uuid = player.getUuid();
        String nicked = String.valueOf(player.isNicked());
        String autoNick = String.valueOf(player.usesAutoNick());
        UUID nickedAs = player.getNickedAs();

        String stmt = "UPDATE " + TABLE_NAME + " SET " + ATTRIBUTE_NICKED + "=?, " + ATTRIBUTE_AUTO_NICK + "=?, " + ATTRIBUTE_NICKED_AS + "=? WHERE " + ATTRIBUTE_UUID + "=?";
        sql.executeUpdateAsync(stmt, Arrays.asList(nicked, autoNick, nickedAs, uuid));

    }

    public void updateNickName(NickPlayer player) {

        UUID uuid = player.getUuid();
        UUID nickedAs = player.getNickedAs();

        String stmt = "UPDATE " + TABLE_NAME + " SET " + ATTRIBUTE_NICKED_AS + "=? WHERE " + ATTRIBUTE_UUID + "=?";
        sql.executeUpdateAsync(stmt, Arrays.asList(nickedAs, uuid));

    }

    public boolean isPlayerRegistered(UUID uuid) {
        return sql.existResult(TABLE_NAME, ATTRIBUTE_UUID, uuid);
    }

    public NickPlayer getNickPlayer(UUID uuid) {

        String stmt = "SELECT * FROM " + TABLE_NAME + " WHERE " + ATTRIBUTE_UUID + "=?";

        ResultSet rs = sql.getResult(stmt, Arrays.asList(uuid));

        try {
            if(rs.next()) {

                boolean nicked = Boolean.valueOf(rs.getString(ATTRIBUTE_NICKED));
                boolean autoNick = Boolean.valueOf(rs.getString(ATTRIBUTE_AUTO_NICK));
                UUID nickedAs = UUID.fromString(rs.getString(ATTRIBUTE_NICKED_AS));

                NickPlayer nickPlayer = new NickPlayer(uuid, nicked, autoNick, nickedAs);
                return nickPlayer;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    public boolean isNickInUse(UUID uuid) {
        return sql.existResult(TABLE_NAME, ATTRIBUTE_NICKED_AS, uuid);
    }

}
