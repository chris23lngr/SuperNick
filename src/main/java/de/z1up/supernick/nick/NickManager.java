package de.z1up.supernick.nick;

import de.z1up.supernick.SuperNick;
import de.z1up.supernick.mysql.SQL;
import de.z1up.supernick.mysql.SQLConfig;
import de.z1up.supernick.util.Messages;
import de.z1up.supernick.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class NickManager {

    public static NickManager instance;

    private SQL sql;

    private NickWrapper nickWrapper;

    private ArrayList<String> nickNames;

    public NickManager() {

        createSQL();

        init();

        readNickNames();

    }

    void createSQL() {
        SQLConfig config = new SQLConfig();
        sql = new SQL(config.readData());
        sql.connect();
    }

    void init() {

        instance = this;

        nickWrapper = new NickWrapper(sql);

        nickNames = new ArrayList<>();
    }

    public NickWrapper getNickWrapper() {
        return nickWrapper;
    }

    void setConfig() {
        SuperNick.getInstance().getConfig().options().copyDefaults(true);
        SuperNick.getInstance().getConfig().addDefault("Nick names",
                Messages.playerExamples);
        SuperNick.getInstance().saveConfig();
    }

    void readNickNames() {
        setConfig();
        nickNames = (ArrayList<String>) SuperNick.getInstance().getConfig().getStringList("Nick names");
    }


    public UUID getRandomNickName() {

        int max = nickNames.size() - 1;
        int pos = new Random().nextInt(max);

        String nick = nickNames.get(pos);

        if(!UUIDFetcher.existsPlayer(nick)) {
            Bukkit.getConsoleSender().sendMessage("§4[ERROR] An error occurred when translating a nickname to a uuid.");
            return null;
        } else {

            UUID uuid = UUIDFetcher.getUUID(nick);

            if(getNickWrapper().isNickInUse(uuid)) {
                return getRandomNickName();
            }

            return uuid;
        }
    }

    public String nickPlayer(NickPlayer nickPlayer) {

        nickPlayer.setNicked(true);
        nickPlayer.update();

        UUID uuid = nickPlayer.getUuid();
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline()) {
            Bukkit.getConsoleSender().sendMessage("§4[ERROR] An error occurred when nicking a player.");
            return null;
        }

        UUID nickUUID = getRandomNickName();
        if(nickUUID == null) {
            Bukkit.getConsoleSender().sendMessage("§4[ERROR] An error occurred when nicking a player.");
            return null;
        }
        String nickName = UUIDFetcher.getName(nickUUID);

        player.setDisplayName(nickName);
        player.setPlayerListName(nickName);

        return nickName;
    }

    public void unnickPlayer(NickPlayer nickPlayer) {

        nickPlayer.setNicked(false);
        nickPlayer.update();

        UUID uuid = nickPlayer.getUuid();
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline()) {
            Bukkit.getConsoleSender().sendMessage("§4[ERROR] An error occurred when unnnicking a player.");
            return;
        }

        String name = player.getName();
        player.setDisplayName(name);
        player.setPlayerListName(name);
    }


}
