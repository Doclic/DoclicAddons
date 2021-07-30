package me.doclic.minecraft.mods.addons.hypixel.api;

import com.google.gson.JsonObject;

public class APIKeyInfo implements HypixelAPI.Request {

    private boolean success = false;
    private JsonObject record = null;

    @Override
    public boolean isSuccessful() { return success; }
    public JsonObject getInfo() { return record; }

}
