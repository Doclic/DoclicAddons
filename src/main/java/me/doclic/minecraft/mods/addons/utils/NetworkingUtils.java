package me.doclic.minecraft.mods.addons.utils;

import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

/**
 * Utilities for networking
 */
public class NetworkingUtils {

    /**
     * Caching the names from UUIDs
     */
    private static final HashMap<String, String> UUID_NAME_CACHE = new HashMap<String, String>();

    /**
     * Gets a Player's name from their UUID
     *
     * @param uuid The Player's UUID without dashes
     * @return The Player's name
     */
    public static String getNameFromUUID(String uuid) {

        // Cache
        uuid = uuid.replace("-", "");
        if (UUID_NAME_CACHE.containsKey(uuid)) return UUID_NAME_CACHE.get(uuid);

        // Getting the URL
        final String json;
        try {
            json = IOUtils.toString(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid));
        } catch (IOException exception) {
            exception.printStackTrace();
            return uuid;
        }

        // If the URL is valid, return the Player's name
        if (!json.isEmpty()) {

            final String name = new JsonParser().parse(json).getAsJsonObject().get("name").getAsString();
            UUID_NAME_CACHE.put(uuid, name);
            return name;

        }

        // Otherwise, return the Player's uuid
        return uuid;

    }

    /**
     * Gets a Player's name from their UUID
     *
     * @param uuid The Player's UUID
     * @return The Player's name
     */
    public static String getNameFromUUID(UUID uuid) {

        // Using the method with a String UUID
        return getNameFromUUID(uuid.toString());

    }

}
