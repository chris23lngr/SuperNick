package de.z1up.supernick.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.z1up.supernick.packet.Reflects;
import de.z1up.supernick.skin.Skin;
import de.z1up.supernick.util.UUIDFetcher;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class NickUtils extends Reflects {

    /*
    public void setName(Player player, String name) {

        PacketPlayOutPlayerInfo removePlayer = new PacketPlayOutPlayerInfo();
        setValue(removePlayer, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        CraftPlayer cp = (CraftPlayer) player;

        setValue(removePlayer, "b", Arrays.asList(new PacketPlayOutPlayerInfo.PlayerInfoData[]{removePlayer.new PlayerInfoData(cp.getProfile(), cp.getHandle().getAirTicks(), WorldSettings.EnumGamemode.valueOf(player.getGameMode().toString()), cp.getHandle().getScoreboardDisplayName())}));

        sendPacket(removePlayer);

        PacketPlayOutPlayerInfo addPlayer = new PacketPlayOutPlayerInfo();
        setValue(addPlayer, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);

        GameProfile profile = cp.getHandle().getProfile();

        setValue(profile, "name", name);

        try {
            Field field = profile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(profile, "rewinside");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        setValue(addPlayer, "b", Arrays.asList(new PacketPlayOutPlayerInfo.PlayerInfoData[]{removePlayer.new PlayerInfoData(cp.getProfile(), cp.getHandle().getAirTicks(), WorldSettings.EnumGamemode.valueOf(player.getGameMode().toString()), new ChatMessage(name))}));

        sendPacket(addPlayer);

        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(((CraftPlayer) player).getHandle().getId());

        for(Player all : Bukkit.getOnlinePlayers()) {
            if(all != player) {
                sendPacket(all, destroy);
            }
        }



        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());

        for(Player all : Bukkit.getOnlinePlayers()) {
            if(all != player) {
                sendPacket(all, spawn);
            }
        }



    }*/

    public void nickPlayer(Player player, String name) {

        CraftPlayer craftPlayer = (CraftPlayer) player;

        removePlayer(craftPlayer);
        destroyPlayer(craftPlayer);

        GameProfile profile = craftPlayer.getHandle().getProfile();

        changeGameProfileName(profile, name);
        changeGameProfileSkin(profile, UUIDFetcher.getUUID(name));

        addPlayer(craftPlayer, profile);
        spawnPlayer(craftPlayer);

    }

    private void removePlayer(CraftPlayer craftPlayer) {

        final PacketPlayOutPlayerInfo removePlayer = new PacketPlayOutPlayerInfo();

        setValue(removePlayer, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(removePlayer, "b", Arrays.asList(removePlayer.new PlayerInfoData(craftPlayer.getHandle().getProfile(),
                craftPlayer.getHandle().getAirTicks(),
                WorldSettings.EnumGamemode.valueOf(craftPlayer.getGameMode().toString()),
                craftPlayer.getHandle().getPlayerListName())));

        sendPacket(removePlayer, craftPlayer);

    }

    private void addPlayer(CraftPlayer craftPlayer, GameProfile profile) {

        final PacketPlayOutPlayerInfo addPlayer = new PacketPlayOutPlayerInfo();

        setValue(addPlayer, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(addPlayer, "b", Arrays.asList(addPlayer.new PlayerInfoData(profile,
                craftPlayer.getHandle().getAirTicks(),
                WorldSettings.EnumGamemode.valueOf(craftPlayer.getGameMode().toString()),
                new ChatMessage(profile.getName()))));

        sendPacket(addPlayer, craftPlayer);
    }

    private void destroyPlayer(CraftPlayer craftPlayer) {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(craftPlayer.getHandle().getId());
        sendPacket(destroy, craftPlayer);
    }

    private void spawnPlayer(CraftPlayer craftPlayer) {
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle());
        sendPacket(spawn, craftPlayer);
    }

    private void changeGameProfileName(GameProfile profile, String name) {
        profile.getProperties().removeAll("name");
        setValue(profile, "name", name);
    }

    private void changeGameProfileSkin(GameProfile profile, UUID uuid) {
        profile.getProperties().removeAll("textures");

        Skin skin = new Skin(uuid);

        if(skin != null) {
            Property property = new Property(skin.getName(), skin.getValue(), skin.getSignature());
            profile.getProperties().put(skin.getName(), property);
        }

    }

}
