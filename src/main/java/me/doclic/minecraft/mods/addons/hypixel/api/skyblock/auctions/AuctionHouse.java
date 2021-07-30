package me.doclic.minecraft.mods.addons.hypixel.api.skyblock.auctions;

import com.google.gson.JsonArray;
import me.doclic.minecraft.mods.addons.hypixel.api.HypixelAPI;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class AuctionHouse implements HypixelAPI.Request {

    private boolean success = false;
    private int page = 0;
    private int totalPages = 0;
    private int totalAuctions = 0;
    private long lastUpdated = 0;
    private JsonArray auctions = null;

    @Override
    public boolean isSuccessful() { return success; }
    public int getPage() { return page; }
    public int getTotalPages() { return totalPages; }
    public int getTotalAuctions() { return totalAuctions; }
    public long getLastUpdated() { return lastUpdated; }
    public JsonArray getAuctions() { return auctions; }

}
