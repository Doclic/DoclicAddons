package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

public class DoclicAddonsHelpCommand extends CommandBase {

    private final String[][][] commands = {

            {

                    {getCommandUsage(null), "Shows this message"},
                    {"setnbttooltipmaxlength <length>", "Sets the max length of the NBT in the item tooltip if toggled on"},
                    {"togglenbtcopying", "Toggles on and off the copying of the item you're hovering's NBT."},
                    {"togglenbttooltip", "Toggles on and off showing the NBT of the item you're hovering in the tooltip."},
                    {"light", "Sets your brightness to a very high value that basically grants you night-vision!"},
                    {"auctionbot <start OR stop>", "Allows you to start or stop Auction Bot"}

            }

    };

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "doclicaddonshelp";
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return getCommandName() + " [page]";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {

        // Errors
        if (args.length > 1) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage(sender)));
            return;

        }

        /// Parsing
        int page;
        try {

            //// Parse the number
            page = Integer.parseInt(args[0]);

            //// Errors
            if (page <= 0) {

                sender.addChatMessage(TextUtils.formatText(ChatColor.RED + args[0] + " is too low!"));
                return;

            } else if (page > commands.length) {

                sender.addChatMessage(TextUtils.formatText(ChatColor.RED + args[0] + " is too high!\n" + ChatColor.RED + "Last page: " + commands.length));
                return;

            }

        } catch (final NumberFormatException e) {
            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + args[0] + " is not a number!"));
            return;
        } catch (final ArrayIndexOutOfBoundsException e) {
            page = 1;
        }

        // Setting the value
        ConfigurationManager.setNbtTooltipMaxLength(page);

        // Sending a top message
        final IChatComponent lines = TextUtils.formatText(ChatColor.BLUE + "----------" + ChatColor.GOLD + " [Help, page " + page + ']' + ChatColor.BLUE + "----------");
        final IChatComponent emptyMessage = TextUtils.formatText(ChatColor.BLACK + " ");
        sender.addChatMessage(lines);
        sender.addChatMessage(emptyMessage);

        // Sending the commands' usage
        for (final String[] command : commands[page - 1])
            sender.addChatMessage(TextUtils.formatText(ChatColor.BLUE + " - /" + command[0] + ": " + command[1]));

        // Sending a bottom message
        sender.addChatMessage(emptyMessage);
        sender.addChatMessage(lines);

    }

}
