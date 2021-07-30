package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class SetNBTTooltipMaxLengthCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand ( final ICommandSender sender ) { return true; }

    @Override
    public String getCommandName () { return "setnbttooltipmaxlength"; }

    @Override
    public String getCommandUsage ( final ICommandSender sender ) { return getCommandName () + " <length>"; }

    @Override
    public void processCommand ( final ICommandSender sender , final String [] args ) {

        // Errors
        if ( args.length == 0 ) {

            sender.addChatMessage ( TextUtils.formatText ( ChatColor.RED + "Not enough arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage ( sender ) ) );
            return;

        }

        if ( args.length != 1 ) {

            sender.addChatMessage ( TextUtils.formatText ( ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage ( sender ) ) );
            return;

        }

        /// Parsing
        final int VALUE;
        try { VALUE = Integer.parseInt ( args [ 0 ] ); }
        catch ( final NumberFormatException e ) { sender.addChatMessage ( TextUtils.formatText ( ChatColor.RED + args [ 0 ] + " is not a number!" ) ); return; }

        // Setting the value
        ConfigurationManager.setNbtTooltipMaxLength ( VALUE );

        // Sending a confirmation message
        sender.addChatMessage ( TextUtils.formatText ( "\u00a7aThe maximum length of the shown NBT set to " + args [ 0 ] + "!" ) );

    }

}
