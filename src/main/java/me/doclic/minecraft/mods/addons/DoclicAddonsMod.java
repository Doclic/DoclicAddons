package me.doclic.minecraft.mods.addons;

import me.doclic.minecraft.mods.addons.commands.*;
import me.doclic.minecraft.mods.addons.hypixel.commands.APIKeyCommand;
import me.doclic.minecraft.mods.addons.hypixel.skyblock.AuctionBot;
import me.doclic.minecraft.mods.addons.hypixel.skyblock.commands.AuctionBotCommand;
import me.doclic.minecraft.mods.addons.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
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
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * Mod class
 */
@Mod(
        modid = DoclicAddonsMod.MOD_ID,
        version = DoclicAddonsMod.VERSION,
        clientSideOnly = true,
        canBeDeactivated = true
)
public class DoclicAddonsMod {

    /**
     * The ID for the Mod
     */
    public static final String MOD_ID = "doclicaddons";
    /**
     * The current version
     */
    public static final String VERSION = "vPre-21.07a";

    /**
     * Directly reference a log4j logger.
    */
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Preventing lag
    */
    private boolean wasCTRLPress, wasShiftPress = false;

    /**
     * Used in the Tooltip for when NBT is bigger than the max length
    */
    private final String lengthCommandUsage;

    private static int lastSlot = Integer.MIN_VALUE;
    private static String lastNBT = null;
    private static int tooltipFirstLine = 0;

    /**
     * Registering
    */
    public DoclicAddonsMod() {

        System.setProperty("java.awt.headless", "false");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register commands
        ClientCommandHandler.instance.registerCommand(new DoclicAddonsHelpCommand());
        ClientCommandHandler.instance.registerCommand(new LightCommand());
        final SetNBTTooltipMaxLengthCommand lengthCommand = new SetNBTTooltipMaxLengthCommand();
        ClientCommandHandler.instance.registerCommand(lengthCommand);
        ClientCommandHandler.instance.registerCommand(new ToggleNBTCopyingCommand());
        ClientCommandHandler.instance.registerCommand(new ToggleNBTTooltipCommand());

        ClientCommandHandler.instance.registerCommand(new APIKeyCommand());
        ClientCommandHandler.instance.registerCommand(new AuctionBotCommand());

        // KeyBindings
        new KeyBindings();

        // Setting the Length Command Usage
        lengthCommandUsage = lengthCommand.getCommandUsage(null);

        // Initiating the storage files
        try { MCIOUtils.initiate(); }
        catch (IOException e) { e.printStackTrace(); }

    }

    /**
     * Listener for {@link FMLInitializationEvent}
     *
     * @param e The Event instance
     */
    @EventHandler
    public void init(final FMLInitializationEvent e) {

        // Config
        ConfigurationManager.createVariables();

        // Activating Auction Bot
        AuctionBot.enable();

    }

    /**
     * Listener for {@link ItemTooltipEvent}
     *
     * @param e The Event instance
     */
    @SubscribeEvent
    public void itemToolTipEvent(ItemTooltipEvent e) {

        // Getting variables
        final ItemStack itemStack = e.itemStack;
        final NBTTagCompound compound = itemStack.serializeNBT();
        final String nbt = compound.toString();
        final Container container = e.entityPlayer.openContainer;
        final List<ItemStack> itemStacks = container.inventoryItemStacks;

        // Copying NBT
        // If the Client hasn't used F3+H, return
        if (!Minecraft.getMinecraft().gameSettings.advancedItemTooltips) return;

        // Free Cookie owner
        final String cookieOwner = NetworkingUtils.getNameFromUUID(compound.getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("cookie_free_player_id"));
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
        if (ConfigurationManager.isAllowingCopy()) {

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

        // Scrolling
        if (!nbt.equals(lastNBT)) {

            tooltipFirstLine = 0;
            lastNBT = nbt;

        }

        else for (final Slot slot : container.inventorySlots)
            if (itemStacks.get(slot.slotNumber) == itemStack) {

                if (lastSlot == slot.slotNumber) break;

                tooltipFirstLine = 0;
                lastSlot = slot.slotNumber;
                break;

            }

        final int scroll = Mouse.getDWheel();
        if (scroll < 0 && tooltipFirstLine != 0) tooltipFirstLine--;
        else if (scroll > 0) tooltipFirstLine++;

        if (tooltipFirstLine >= e.toolTip.size() - 1) tooltipFirstLine = e.toolTip.size() - 1;
        if (tooltipFirstLine > 0)
            e.toolTip.subList(0, tooltipFirstLine).clear();
        if (e.toolTip.size() > 20)
            e.toolTip.subList(20, e.toolTip.size()).clear();

    }

}
