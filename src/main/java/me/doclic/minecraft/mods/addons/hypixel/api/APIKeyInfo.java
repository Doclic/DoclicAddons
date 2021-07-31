package me.doclic.minecraft.mods.addons.hypixel.api;

import com.google.gson.JsonObject;

/**
 * The {@link HypixelAPI.Request} for the API Key info, used to test keys
 */
public class APIKeyInfo implements HypixelAPI.Request {

    /**
     * Field set automatically using Reflection
     */
    private boolean success = false;
    /**
     * Field set automatically using Reflection
     */
    private JsonObject record = null;

    /**
     * Returns true if the request to the Hypixel API request was successful
     *
     * @return true if the request to the Hypixel API request was successful
     */
    @Override
    public boolean isSuccessful() { return success; }
    /**
     * Returns information about the API Key
     *
     * @return information about the API Key
     */
    public JsonObject getInfo() { return record; }

}
