package de.z1up.supernick.command;

import de.z1up.supernick.SuperNick;
import de.z1up.supernick.nick.NickManager;
import de.z1up.supernick.nick.NickPlayer;
import de.z1up.supernick.util.Messages;
import de.z1up.supernick.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandCheckNick implements CommandExecutor, TabCompleter {

    private final String NAME = "checknick";
    private final String PERM = "nick.checknick";
    private final String USAGE = "/checknick <Player>";

    public CommandCheckNick() {
        SuperNick.getInstance().getCommand(NAME).setExecutor(this::onCommand);
        SuperNick.getInstance().getCommand(NAME).setPermission(PERM);
        SuperNick.getInstance().getCommand(NAME).setPermissionMessage(Messages.NO_PERM);
        SuperNick.getInstance().getCommand(NAME).setUsage(USAGE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(args.length < 1)  {
            player.sendMessage(Messages.WRONG_USAGE + command.getUsage());
            return true;
        }

        String targetTag = args[0];

        if(Bukkit.getPlayer(targetTag) == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        Player target = Bukkit.getPlayer(targetTag);

        if(!target.isOnline()) {
            player.sendMessage(Messages.COMMAND_NOT_EXECUTABLE);
            return true;
        }

        UUID targetUUID = target.getUniqueId();

        boolean nicked = false;

        String actualname = "";

        if(NickManager.instance.getNickWrapper().isPlayerRegistered(targetUUID)) {
            NickPlayer nick = NickManager.instance.getNickWrapper().getNickPlayer(targetUUID);
            if(nick.isNicked()) {
                nicked = true;
                actualname = ""+ nick.getUuid();
            }
        }

        player.sendMessage((nicked ? ("§c" + actualname + Messages.TARGET_NICKED_AS + target.getName())
                : Messages.TARGET_NOT_NICKED));

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completes = new ArrayList<>();

        if(args.length >= 1) {
            Bukkit.getOnlinePlayers().forEach(player -> completes.add(player.getName()));
        }

        return completes;
    }
}
