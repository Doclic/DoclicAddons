package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class ToggleNBTCopyingCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand ( final ICommandSender sender ) { return true; }

    @Override
    public String getCommandName () { return "togglenbtcopying"; }

    @Override
    public String getCommandUsage ( final ICommandSender sender ) { return getCommandName (); }

    @Override
    public void processCommand ( final ICommandSender sender , final String [] args ) {

        // Errors
        if ( args.length != 0 ) {

            sender.addChatMessage ( TextUtils.formatText ( ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage ( sender ) ) );
            return;

        }

        // Getting the old value and toggling it
        final boolean value = ! ConfigurationManager.isAllowingCopy ();

        // Setting the value
        ConfigurationManager.allowCopy ( value );

        // Sending a confirmation message
        sender.addChatMessage ( TextUtils.formatText ( ChatColor.GREEN + "Toggled " + ( ( value ) ? "on" : "off" ) + " the copy of NBT using Shift or Control!" ) );

    }

}
