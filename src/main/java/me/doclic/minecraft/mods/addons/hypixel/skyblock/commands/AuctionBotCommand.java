package me.doclic.minecraft.mods.addons.hypixel.skyblock.commands;

import me.doclic.minecraft.mods.addons.hypixel.skyblock.AuctionBot;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * /auctionbot Command class
 */
public class AuctionBotCommand extends CommandBase {

    /**
     * Always returns true
     *
     * @param sender This parameter is just there for the Override
     * @return true
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }

    /**
     * Returns the name of the command
     *
     * @return "auctionbot"
     */
    @Override
    public String getCommandName() { return "auctionbot"; }

    /**
     * Returns the usage for this command
     *
     * @param sender This parameter is just there for the Override
     * @return "auctionbot <start OR stop OR help>"
     */
    @Override
    public String getCommandUsage(ICommandSender sender) { return getCommandName() + " <start OR stop OR help>"; }

    /**
     * Called when /auctionbot is used
     *
     * @param sender The sender of the command
     * @param args The arguments of the command
     */
    @Override
    public void processCommand(final ICommandSender sender, String[] args) {

        // Errors
        if (args.length == 0) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Not enough arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage(sender)));
            return;

        }

        if (args.length != 1) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage(sender)));
            return;

        }

        // Sub-commands
        switch (SubCommand.getSubCommand(args[0])) {

            case START:
                AuctionBot.start();
                sender.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "Started the Auction Bot"));

                break;

            case STOP:
                AuctionBot.stop();
                sender.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "Stopped the Auction Bot"));

                break;

            case DEBUG:
                sender.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "Checking the Auction House..."));
                AuctionBot.checkAsync(new Runnable() {@Override public void run() {
                    sender.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "Checked the Auction House!"));
                }});

                break;

            case HELP:
                sender.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "The Auction Bot is a tool that scouts the auction house and send you a message when finding a good deal.\nTo use it you need to set your api key using /apikey."));

                break;

            default:
                sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Invalid sub-command!"));

                break;

        }

    }

    private enum SubCommand {

        DEFAULT,

        START,
        STOP,

        DEBUG,

        HELP;

        public static SubCommand getSubCommand(String name) {

            if (name.equalsIgnoreCase("START")) return START;
            else if (name.equalsIgnoreCase("STOP")) return STOP;

            else if (name.equalsIgnoreCase("DEBUG")) return DEBUG;

            else if (name.equalsIgnoreCase("HELP") || name.equalsIgnoreCase("?")) return HELP;

            else return DEFAULT;

        }

    }

}
