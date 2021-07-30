package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class LightCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand ( final ICommandSender sender ) { return true; }

    @Override
    public String getCommandName () { return "light"; }

    @Override
    public String getCommandUsage ( final ICommandSender sender ) { return getCommandName (); }

    @Override
    public void processCommand ( final ICommandSender sender , final String [] args ) {

        // Errors
        if ( args.length != 0 ) {

            sender.addChatMessage ( TextUtils.formatText ( ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage ( sender ) ) );
            return;

        }

        // Setting the value
        Minecraft.getMinecraft ().gameSettings.gammaSetting = 69;

        // Sending a confirmation message
        sender.addChatMessage ( TextUtils.formatText ( ChatColor.GREEN + "Set your gamma to 69! (which basically grants you night vision)" ) );

    }

}
