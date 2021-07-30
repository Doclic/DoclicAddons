package me.doclic.minecraft.mods.addons.hypixel.commands;

import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.hypixel.api.HypixelAPI;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class APIKeyCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }

    @Override
    public String getCommandName() { return "apikey"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return getCommandName() + " <key>"; }

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
