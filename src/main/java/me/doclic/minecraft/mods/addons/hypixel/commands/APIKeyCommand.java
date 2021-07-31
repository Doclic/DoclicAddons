package me.doclic.minecraft.mods.addons.hypixel.commands;

import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.hypixel.api.HypixelAPI;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * /apikey Command class
 */
public class APIKeyCommand extends CommandBase {

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
     * @return "apikey"
     */
    @Override
    public String getCommandName() { return "apikey"; }

    /**
     * Returns the usage for this command
     *
     * @param sender This parameter is just there for the Override
     * @return "apikey <key>"
     */
    @Override
    public String getCommandUsage(ICommandSender sender) { return getCommandName() + " <key>"; }

    /**
     * Called when /apikey is used
     *
     * @param sender The sender of the command
     * @param args The arguments of the command
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        // Errors
        if (args.length == 0) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Not enough arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage(sender)));
            return;

        }

        if (args.length != 1) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage(sender)));
            return;

        }

        // Testing the API Key
        final String oldAPIKey = ConfigurationManager.getAPIKey();
        ConfigurationManager.setApiKey(args[0]);
        if (!HypixelAPI.isCurrentAPIKeyValid()) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "This API Key isn't valid!"));
            ConfigurationManager.setApiKey(oldAPIKey);
            return;

        }

        // Sending a confirmation message
        sender.addChatMessage(TextUtils.formatText(ChatColor.GREEN + "Set your API Key to " + args[0]));

    }

}
