package de.z1up.supernick.command;

import de.z1up.supernick.SuperNick;
import de.z1up.supernick.nick.NickManager;
import de.z1up.supernick.nick.NickPlayer;
import de.z1up.supernick.nick.NickUtils;
import de.z1up.supernick.util.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandNick implements CommandExecutor {

    private final String NAME = "nick";
    private final String PERM = "nick.use";

    public CommandNick() {
        SuperNick.getInstance().getCommand(NAME).setExecutor(this::onCommand);
        SuperNick.getInstance().getCommand(NAME).setPermission(PERM);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }

        String prefix = SuperNick.getInstance().getPrefix();

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(!NickManager.instance.getNickWrapper().isPlayerRegistered(uuid)) {
            player.sendMessage(prefix + "§cCannot execute this command at this time. Try again later.");
            return true;
        }

        NickPlayer nickPlayer = NickManager.instance.getNickWrapper().getNickPlayer(uuid);

        if(nickPlayer.isNicked()) {

            NickManager.instance.unnickPlayer(nickPlayer);
            player.sendMessage(prefix + "§7You are now no longer nicked.");
            return true;
        }

        UUID nickUUID = NickManager.instance.getRandomNickName();
        String nickName = UUIDFetcher.getName(nickUUID);

        new NickUtils().nickPlayer(player, nickName);

        player.sendMessage(prefix + "§7You are now playing as §a" + nickName);

        return false;
    }
}
