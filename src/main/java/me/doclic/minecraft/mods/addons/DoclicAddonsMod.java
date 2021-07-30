package me.doclic.minecraft.mods.addons;

import me.doclic.minecraft.mods.addons.commands.*;
import me.doclic.minecraft.mods.addons.hypixel.commands.APIKeyCommand;
import me.doclic.minecraft.mods.addons.hypixel.skyblock.AuctionBot;
import me.doclic.minecraft.mods.addons.hypixel.skyblock.commands.AuctionBotCommand;
import me.doclic.minecraft.mods.addons.hypixel.skyblock.utils.SkyblockUtils;
import me.doclic.minecraft.mods.addons.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(modid = DoclicAddonsMod.MOD_ID, version = DoclicAddonsMod.VERSION, clientSideOnly = true, canBeDeactivated = true)
public class DoclicAddonsMod {

    public static final String MOD_ID = "doclicaddons";
    public static final String VERSION = "21.07a (BETA)";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Preventing spam
    private boolean wasCTRLPress, wasShiftPress = false;

    // Used in the Tooltip for when NBT is bigger than the max length
    private final String lengthCommandUsage;

    // Registering
    public DoclicAddonsMod() {

        System.setProperty("java.awt.headless", "false");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register commands
        ClientCommandHandler.instance.registerCommand(new DoclicAddonsHelpCommand());
        ClientCommandHandler.instance.registerCommand(new LightCommand());
        final SetNBTTooltipMaxLengthCommand lengthCommand = new SetNBTTooltipMaxLengthCommand();
        ClientCommandHandler.instance.registerCommand(new SetNBTTooltipMaxLengthCommand());
        ClientCommandHandler.instance.registerCommand(new ToggleNBTCopyingCommand());
        ClientCommandHandler.instance.registerCommand(new ToggleNBTTooltipCommand());

        ClientCommandHandler.instance.registerCommand(new APIKeyCommand());
        ClientCommandHandler.instance.registerCommand(new AuctionBotCommand());

        // Proxy
        new KeyBindings();

        // Setting the Length Command Usage
        lengthCommandUsage = lengthCommand.getCommandUsage(null);

        // Initiating the storage files
        try { MCIOUtils.initiate(); }
        catch (IOException e) { e.printStackTrace(); }

    }

    @EventHandler
    public void init(final FMLInitializationEvent e) {

        // Config
        ConfigurationManager.createVariables();

        // Activating Auction Bot
        AuctionBot.enable();

    }

    @SubscribeEvent
    public void itemToolTipEvent(ItemTooltipEvent e) {

        // If the Client hasn't used F3+H, return
        if (!Minecraft.getMinecraft().gameSettings.advancedItemTooltips) {
            return;
        }

        // Getting variables
        final ItemStack itemStack = e.itemStack;
        final NBTTagCompound compound = itemStack.serializeNBT();
        final String nbt = compound.toString();

        // Free Cookie owner
        final String cookieOwner = SkyblockUtils.getCookieOwner(compound.getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("cookie_free_player_id"));
        if (cookieOwner != null)
            e.toolTip.add(ChatColor.GRAY + "Free Cookie Owner: " + ChatColor.DARK_GRAY + cookieOwner);

        // Showing NBT Tags
        final int maximumLength = ConfigurationManager.getNBTTooltipMaxLength();
        if (maximumLength > 0 && ConfigurationManager.isShowingNBTTooltip()) {

            e.toolTip.add(" ");
            e.toolTip.add(ChatColor.DARK_GRAY + "NBT:");

            if (nbt.length() <= maximumLength)
                e.toolTip.add(ChatColor.DARK_GRAY + nbt);

            else {

                e.toolTip.add(ChatColor.DARK_GRAY + nbt.substring(0, maximumLength) + ChatColor.DARK_GRAY + "  ...");
                e.toolTip.add(" ");
                e.toolTip.add(ChatColor.DARK_GRAY + "(length capped at " + maximumLength + ", use /" + lengthCommandUsage + " to change it)");

            }

        }

        // Copying to Clipboard
        if (!ConfigurationManager.isAllowingCopy()) return;

        /// Adding the help
        e.toolTip.add(" ");
        e.toolTip.add(ChatColor.DARK_GRAY + "Press " + ChatColor.YELLOW + "CTRL" + ChatColor.DARK_GRAY + " to copy the NBT.");
        final NBTTagCompound texturesCompoundTag = compound.getCompoundTag("tag").getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", 10).getCompoundTagAt(0);
        final boolean isPlayerSkull =
                itemStack.getItem() instanceof ItemSkull &&
                        itemStack.getItemDamage() == 3 && (
                                texturesCompoundTag.hasKey("Values") ||
                                        texturesCompoundTag.hasKey("Signature")
                        );
        if (isPlayerSkull)
            e.toolTip.add(ChatColor.DARK_GRAY + "Press " + ChatColor.YELLOW + "SHIFT" + ChatColor.DARK_GRAY + " to copy the Skull Owner Texture tag.");

        if (GuiScreen.isCtrlKeyDown() && !wasCTRLPress) {

            wasCTRLPress = true;

            if (TextUtils.copyToClipboard(nbt)) {

                e.entityPlayer.addChatMessage(TextUtils.formatText(ChatColor.GREEN + "Copied the NBT to clipboard!"));
                LOGGER.info("Copied NBT to clipboard");

            }

        } else if (!GuiScreen.isCtrlKeyDown()) wasCTRLPress = false;

        // Copying Skull Texture to Clipboard
        if (GuiScreen.isShiftKeyDown() && isPlayerSkull && !wasShiftPress) {

            wasShiftPress = true;

            /// Getting the Skull Texture
            String headTexture = texturesCompoundTag.getString("Signature");
            if (headTexture.isEmpty()) headTexture = texturesCompoundTag.getString("Value");

            /// Copying to it to Clipboard
            if (!headTexture.isEmpty())
                if (TextUtils.copyToClipboard(headTexture)) {

                    e.entityPlayer.addChatMessage(TextUtils.formatText(ChatColor.GREEN + "Copied the Skull Owner texture NBT Tag to clipboard!"));
                    LOGGER.info("Copied the Skull Owner texture NBT Tag to clipboard");

                }

        } else if (!GuiScreen.isShiftKeyDown()) wasShiftPress = false;

    }

}
