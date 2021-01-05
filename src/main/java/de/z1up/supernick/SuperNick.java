package de.z1up.supernick;

import de.z1up.supernick.command.*;
import de.z1up.supernick.listener.ListenerCommandPreProcess;
import de.z1up.supernick.listener.ListenerPlayerJoin;
import de.z1up.supernick.nick.NickManager;
import de.z1up.supernick.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperNick extends JavaPlugin {

    private static SuperNick        instance;

    @Override
    public void onLoad() {
        super.onLoad();
        init();
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_LOADING);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        register();
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_ENABLING);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_DISABLING);
    }

    void init() {
        instance = this;
        new NickManager();
    }

    void register() {
        new ListenerPlayerJoin();
        new ListenerCommandPreProcess();

        new CommandNick();
        new CommandAutoNick();
        new CommandCheckNick();
        new CommandWhoAmI();
        new CommandUnnick();
    }

    public static SuperNick getInstance() {
        return instance;
    }


}
