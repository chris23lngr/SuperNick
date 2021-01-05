package de.z1up.supernick.util;

// imports
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UUIDFetcher {

    // variables

    private static Gson                     gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final String             UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String             NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static Map<String, UUID>        uuidCache = new HashMap<String, UUID>();
    private static Map<UUID, String>        nameCache = new HashMap<UUID, String>();
    private static ExecutorService          pool = Executors.newCachedThreadPool();

    private String                          name;
    private UUID                            id;

    // methods

    /**
     * Checks if a player exists by theire username.
     *
     * @param name The name of the player that needs to be checked.
     * @return If player exists or not.
     */
    public static boolean existsPlayer(String name) {
        return getUUID(name) != null;
    }
    
    /**
     * Checks if a player exists by theire username.
     *
     * @param uuid The UUID of the player that needs to be checked.
     * @return If player exists or not.
     */
    public static boolean existsPlayer(UUID uuid) {
        return getName(uuid) != null;
    }

    /**
     * Returns the UUID of the playername.
     *
     * @param name The name of the player, whos UUID will be returned.
     */
    public static UUID getUUID(String name) {
        return getUUIDAt(name, System.currentTimeMillis());
    }

    /**
     * Returns the UUID of a player at a specific timestamp, useful to
     * align usernames and UUIDs from the past.
     *
     * @param name The name of the player.
     * @param timestamp The timestamp of when the UUID was connected to the playername.
     * @return The UUID at the specific timestamp.
     */
    public static UUID getUUIDAt(String name, long timestamp) {
        name = name.toLowerCase();
        if (uuidCache.containsKey(name)) {
            return uuidCache.get(name);
        }

        try {
            HttpURLConnection connection =
                    (HttpURLConnection) new URL(String.format(UUID_URL, name, timestamp / 1000)).openConnection();
            connection.setReadTimeout(5000);
            UUIDFetcher data = gson.fromJson(
                    new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);

            if (data == null) {
                return null;
            }

            uuidCache.put(name, data.id);
            nameCache.put(data.id, data.name);
            return data.id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the name of a player by their UUID.
     *
     * @param uuid The UUID of the player whos name needs to be checked.
     * @return The UUID of the checked player.
     */
    public static String getName(UUID uuid) {
        if (nameCache.containsKey(uuid)) {
            return nameCache.get(uuid);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);
            UUIDFetcher[] nameHistory = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher[].class);
            UUIDFetcher currentNameData = nameHistory[nameHistory.length - 1];

            uuidCache.put(currentNameData.name.toLowerCase(), uuid);
            nameCache.put(uuid, currentNameData.name);

            return currentNameData.name;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}