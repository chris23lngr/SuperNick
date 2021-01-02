package de.z1up.supernick;

import de.z1up.supernick.command.CommandAutoNick;
import de.z1up.supernick.command.CommandCheckNick;
import de.z1up.supernick.command.CommandNick;
import de.z1up.supernick.listener.ListenerChat;
import de.z1up.supernick.listener.ListenerPlayerJoin;
import de.z1up.supernick.nick.NickManager;
import de.z1up.supernick.nick.NickWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperNick extends JavaPlugin {

    private final String PREFIX = "§8[§eSuper§6Nick§8]" + " ";

    private static SuperNick instance;

    @Override
    public void onLoad() {
        super.onLoad();
        init();
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "§7Loading SuperNick by z1up.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        register();
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "§7Enabling SuperNick by z1up. " +
                "Running on version " + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "§7Disabling SuperNick by z1up.");
    }

    void init() {
        instance = this;
        new NickManager();
    }

    void register() {
        new ListenerPlayerJoin();
        new ListenerChat();

        new CommandNick();
        new CommandAutoNick();
        new CommandCheckNick();
    }

    public static SuperNick getInstance() {
        return instance;
    }

    public String getPrefix() {
        return PREFIX;
    }

}
