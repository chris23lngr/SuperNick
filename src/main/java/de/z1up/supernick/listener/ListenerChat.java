package de.z1up.supernick.listener;

import de.z1up.supernick.SuperNick;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ListenerChat implements Listener {

    public ListenerChat() {
        Bukkit.getPluginManager().registerEvents(this, SuperNick.getInstance());
    }

    @EventHandler
    public void onCall(AsyncPlayerChatEvent event) {
        event.setFormat("§8[§a" + event.getPlayer().getName() + "§8] §7" + event.getMessage());
    }

}
