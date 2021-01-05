package de.z1up.supernick.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.z1up.supernick.packet.Reflects;
import de.z1up.supernick.skin.Skin;
import de.z1up.supernick.util.UUIDFetcher;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class NickUtils extends Reflects {

    public static void nickPlayer(Player player, String name) {

        CraftPlayer craftPlayer = (CraftPlayer) player;

        removePlayer(craftPlayer);
        destroyPlayer(craftPlayer);

        GameProfile profile = craftPlayer.getHandle().getProfile();

        changeGameProfileName(profile, name);
        changeGameProfileSkin(profile, UUIDFetcher.getUUID(name));

        addPlayer(craftPlayer, profile);
        spawnPlayer(craftPlayer);

        player.setDisplayName(name);

    }

    public static void unnickPlayer(Player player) {
        //if(!player.isNicked()) { return; }
        CraftPlayer cp = (CraftPlayer) player;
        destroyPlayer(cp);
        removePlayer(cp);
        addPlayer(cp, cp.getProfile());
    }

    private static void removePlayer(CraftPlayer craftPlayer) {

        final PacketPlayOutPlayerInfo removePlayer = new PacketPlayOutPlayerInfo();

        setValue(removePlayer, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(removePlayer, "b", Arrays.asList(removePlayer.new PlayerInfoData(craftPlayer.getHandle().getProfile(),
                craftPlayer.getHandle().getAirTicks(),
                WorldSettings.EnumGamemode.valueOf(craftPlayer.getGameMode().toString()),
                craftPlayer.getHandle().getPlayerListName())));

        sendPacketExceptFor(removePlayer, craftPlayer);

    }

    private static void addPlayer(CraftPlayer craftPlayer, GameProfile profile) {

        final PacketPlayOutPlayerInfo addPlayer = new PacketPlayOutPlayerInfo();

        setValue(addPlayer, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(addPlayer, "b", Arrays.asList(addPlayer.new PlayerInfoData(profile,
                craftPlayer.getHandle().getAirTicks(),
                WorldSettings.EnumGamemode.valueOf(craftPlayer.getGameMode().toString()),
                new ChatMessage(profile.getName()))));

        sendPacketExceptFor(addPlayer, craftPlayer);
    }

    private static void destroyPlayer(CraftPlayer craftPlayer) {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(craftPlayer.getHandle().getId());
        sendPacketExceptFor(destroy, craftPlayer);
    }

    private static void spawnPlayer(CraftPlayer craftPlayer) {
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle());
        sendPacketExceptFor(spawn, craftPlayer);
    }

    private static void changeGameProfileName(GameProfile profile, String name) {
        profile.getProperties().removeAll("name");
        setValue(profile, "name", name);
    }

    private static void changeGameProfileSkin(GameProfile profile, UUID uuid) {
        profile.getProperties().removeAll("textures");

        Skin skin = new Skin(uuid);

        if(skin != null) {
            Property property = new Property(skin.getName(), skin.getValue(), skin.getSignature());
            profile.getProperties().put(skin.getName(), property);
        }

    }

}
