package me.doclic.minecraft.mods.addons.hypixel.skyblock;

import com.google.gson.*;
import me.doclic.minecraft.mods.addons.DoclicAddonsMod;
import me.doclic.minecraft.mods.addons.hypixel.api.HypixelAPI;
import me.doclic.minecraft.mods.addons.hypixel.api.skyblock.auctions.AuctionHouse;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.MCIOUtils;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionBot {

    private static final long CHECK_DELAY = 3 * 60 * 1000;
    private static final ArrayList<String> ID_LIST = new ArrayList<String>();

    private static boolean enabled = false;
    private static boolean sendMessages = false;

    private static final File PRICES = new File(MCIOUtils.STORAGE_PATH, "ah_bin.json");


    public static void enable() { enabled = true; checkAsync(); }
    public static void disable() { enabled = false; sendMessages = false; }

    public static void start() { enable(); sendMessages = true; }
    public static void stop() { sendMessages = false; }


    private static void addPrice(String id, int price) {

        if (!PRICES.setWritable(true)) return;

        final JsonObject json;
        try { json = new JsonParser().parse(new FileReader(PRICES)).getAsJsonObject(); }
        catch (FileNotFoundException e) { e.printStackTrace(); return; }
        catch (IllegalStateException e) { e.printStackTrace(); return; }

        final JsonElement element = json.get(id);
        final JsonArray pricesForItem;
        if (element == null || !element.isJsonArray()) {

            pricesForItem = new JsonArray();
            json.add(id, pricesForItem);

        }
        else
            pricesForItem = element.getAsJsonArray();
        pricesForItem.add(new JsonPrimitive(price));
        json.add(id, pricesForItem);

        try {

            final FileWriter writer = new FileWriter(PRICES);
            writer.write(json.toString());
            writer.close();

        } catch (IOException e) { e.printStackTrace(); }

    }

    private static int getAveragePrice(String id) {

        final JsonObject json;
        try { json = new JsonParser().parse(new FileReader(PRICES)).getAsJsonObject(); }
        catch (FileNotFoundException e) { e.printStackTrace(); return -1; }
        catch (IllegalStateException e) { e.printStackTrace(); return -1; }

        final JsonElement element = json.get(id);
        if (element == null || !element.isJsonArray()) return 1; // 1 to prevent division by 0
        final JsonArray pricesForItem = element.getAsJsonArray();
        int i = 0;
        int average = 0;
        for (JsonElement price : pricesForItem) {

            if (!price.isJsonPrimitive()) continue;

            i++;
            average += price.getAsInt();

        }

        return average / i;

    }


    public static void checkAsync() { checkAsync(null); }
    public static void checkAsync(final Runnable runAfter) {

        final Thread thread = new Thread(new Runnable() { @Override public void run() { check(); }});
        thread.start();
        if (runAfter == null) return;
        new Thread(new Runnable() {
            @Override public void run() {

                try { thread.join(); }
                catch (InterruptedException e) { e.printStackTrace(); }
                runAfter.run();

            }
        }).start();

    }

    private static void check() {

        String best = null;
        int bestPercent = Integer.MAX_VALUE;
        int bestPrice = -1;

        for (int i = 0; i < HypixelAPI.getAuctionHouse(0).getTotalPages(); i++) {

            DoclicAddonsMod.LOGGER.info("Auction Bot check: Checking page " + i);

            for (JsonElement auctionElement : HypixelAPI.getAuctionHouse(i).getAuctions()) {

                if (!auctionElement.isJsonObject()) continue;
                final JsonObject auction = auctionElement.getAsJsonObject();
                final String id = auction.get("uuid").getAsString();

                final JsonElement binElement = auction.get("bin");
                final boolean bin = binElement != null && binElement.getAsBoolean();

                if (!bin || ID_LIST.contains(id)) continue;

                final String itemId;
                try {
                    itemId = CompressedStreamTools.readCompressed(new ByteArrayInputStream(
                            Base64.decodeBase64(auction.get("item_bytes").getAsString())
                    )).getTagList("i", 10).getCompoundTagAt(0).getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id");
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                final int price = auction.get("starting_bid").getAsInt();
                final int average = getAveragePrice(itemId);
                final int percent = (price / average) * 100;

                addPrice(itemId, price);

                if (sendMessages && percent < 80 && percent < bestPercent) {

                    bestPercent = percent;
                    best = id;
                    bestPrice = price;

                }

            }

        }

        if (best != null) {

            final ChatComponentText message = new ChatComponentText(ChatColor.GOLD + "[Auction Bot] " + ChatColor.YELLOW + "Found an item " + ChatColor.WHITE + bestPercent + "% " + ChatColor.YELLOW + " under the average price! (price: " + bestPrice + ")");
            final ChatComponentText clickable = new ChatComponentText(ChatColor.YELLOW.toString() + ChatColor.BOLD + "CLICK");
            clickable.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/viewauction " + best)));
            message.appendSibling(clickable);

            Minecraft.getMinecraft().thePlayer.addChatMessage(message);

        } else Minecraft.getMinecraft().thePlayer.addChatMessage(TextUtils.formatText(ChatColor.DARK_GRAY + "No BIN auction was 20% under their average price."));

        DoclicAddonsMod.LOGGER.info("Finished Auction Bot check");

    }

    private static void scheduleCheck() {

        final Timer timer = new Timer();
        timer.schedule(new ScheduledCheck(timer), CHECK_DELAY);

    }


    private static class ScheduledCheck extends TimerTask {

        final Timer timer;

        private ScheduledCheck(Timer timer) {

            this.timer = timer;

        }

        @Override
        public void run() {

            check();

            if (enabled && timer != null) timer.schedule(this, CHECK_DELAY);

        }

    }

}
