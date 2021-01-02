package de.z1up.supernick.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.z1up.supernick.SuperNick;
import de.z1up.supernick.mysql.SQL;
import de.z1up.supernick.mysql.SQLConfig;
import de.z1up.supernick.skin.Skin;
import de.z1up.supernick.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
                        Arrays.asList("Peter123", "myguyjustin", "LumpeHD", "MeinGegner", "APlayer1234"));
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

        setSkin(player, nickUUID);

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

        setSkin(player, uuid);
    }

    public void setSkin(Player player, UUID uuid) {

        GameProfile profile = ((CraftPlayer) player).getProfile();

        profile.getProperties().clear();

        Skin skin = new Skin(uuid);
        if(skin.getName() != null) {
            profile.getProperties().put(skin.getName(), new Property(skin.getName(), skin.getValue(), skin.getSignature()));
        }

        Bukkit.getScheduler().runTaskLater(SuperNick.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(player));
            }
        }, 5);

        Bukkit.getScheduler().runTaskLater(SuperNick.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
            }
        }, 15);
    }

}
