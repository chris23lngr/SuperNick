package de.z1up.supernick.listener;

import de.z1up.supernick.SuperNick;
import de.z1up.supernick.nick.NickManager;
import de.z1up.supernick.nick.NickPlayer;
import de.z1up.supernick.nick.NickUtils;
import de.z1up.supernick.util.Messages;
import de.z1up.supernick.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.UUID;

public class ListenerPlayerJoin implements Listener {

    private final String        PERM = "nick.use";


    public ListenerPlayerJoin() {
        Bukkit.getPluginManager().registerEvents(this, SuperNick.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCall(final PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if(!player.hasPermission(PERM)) {
            return;
        }

        if(!NickManager.instance.getNickWrapper().isPlayerRegistered(player.getUniqueId())) {

            UUID uuid = player.getUniqueId();
            UUID nickedAs = UUID.randomUUID();

            NickPlayer nickPlayer = new NickPlayer(uuid, false, false, nickedAs);
            nickPlayer.insert();

        } else {

            NickPlayer nickPlayer = NickManager.instance.getNickWrapper().getNickPlayer(player.getUniqueId());

            if (nickPlayer == null) {
                Bukkit.getConsoleSender().sendMessage(Messages.PLAYER_REGISTER_ERROR);
                return;
            }

            if (nickPlayer.usesAutoNick()) {
                UUID nickUUID = NickManager.instance.getRandomNickName();
                String nickName = UUIDFetcher.getName(nickUUID);

                NickUtils.nickPlayer(player, nickName);

                nickPlayer.setNicked(true);
                nickPlayer.setNickedAs(nickUUID);
                nickPlayer.update();

                player.sendMessage(Messages.NICKED_AS + nickName);
            }
        }

    }

}
