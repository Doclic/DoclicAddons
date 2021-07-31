package me.doclic.minecraft.mods.addons.hypixel.api.skyblock.auctions;

import com.google.gson.JsonArray;
import me.doclic.minecraft.mods.addons.hypixel.api.HypixelAPI;

/**
 * The {@link HypixelAPI.Request} for the Hypixel Skyblock Auction House
 */
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class AuctionHouse implements HypixelAPI.Request {

    /**
     * Field set automatically using Reflection
     */
    private boolean success = false;
    /**
     * Field set automatically using Reflection
     */
    private int page = 0;
    /**
     * Field set automatically using Reflection
     */
    private int totalPages = 0;
    /**
     * Field set automatically using Reflection
     */
    private int totalAuctions = 0;
    /**
     * Field set automatically using Reflection
     */
    private long lastUpdated = 0;
    /**
     * Field set automatically using Reflection
     */
    private JsonArray auctions = null;

    /**
     * Returns true if the request to the Hypixel API request was successful
     *
     * @return true if the request to the Hypixel API request was successful
     */
    @Override
    public boolean isSuccessful() { return success; }
    /**
     * Returns the page index used for the request (each page has 1000 auctions)
     *
     * @return the page index used for the request (each page has 1000 auctions)
     */
    public int getPage() { return page; }
    /**
     * Returns the total amount of pages in the auction house at the time of the request
     *
     * @return the total amount of pages in the auction house at the time of the request
     */
    public int getTotalPages() { return totalPages; }
    /**
     * Returns the total amount of auctions in the auction house at the time of the request
     *
     * @return the total amount of auctions in the auction house at the time of the request
     */
    public int getTotalAuctions() { return totalAuctions; }
    /**
     * Returns the last time the auction house was updated
     *
     * @return the last time the auction house was updated
     */
    public long getLastUpdated() { return lastUpdated; }
    /**
     * Returns the auctions in the current page
     *
     * @return the auctions in the current page
     */
    public JsonArray getAuctions() { return auctions; }

}
