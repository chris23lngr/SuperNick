package de.z1up.supernick.command;

import de.z1up.supernick.SuperNick;
import de.z1up.supernick.nick.NickManager;
import de.z1up.supernick.nick.NickPlayer;
import de.z1up.supernick.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandAutoNick implements CommandExecutor {

    private final String NAME = "autonick";
    private final String PERM = "nick.use";

    public CommandAutoNick() {
        SuperNick.getInstance().getCommand(NAME).setExecutor(this::onCommand);
        SuperNick.getInstance().getCommand(NAME).setPermission(PERM);
        SuperNick.getInstance().getCommand(NAME).setPermissionMessage(Messages.NO_PERM);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(!NickManager.instance.getNickWrapper().isPlayerRegistered(uuid)) {
            player.sendMessage(Messages.COMMAND_NOT_EXECUTABLE);
            return true;
        }

        NickPlayer nickPlayer = NickManager.instance.getNickWrapper().getNickPlayer(uuid);

        System.out.println(nickPlayer.usesAutoNick());

        boolean enabled = nickPlayer.usesAutoNick();

        if(!enabled) {
            nickPlayer.setAutoNick(true);
        } else {
            nickPlayer.setAutoNick(false);
        }

        nickPlayer.update();

        player.sendMessage((enabled ? Messages.AUTONICK_DEACTIVATED
                : Messages.AUTONICK_ACTIVATED));

        return false;
    }

}
