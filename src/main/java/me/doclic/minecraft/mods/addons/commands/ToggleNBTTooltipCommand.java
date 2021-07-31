package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * /togglenbttooltip Command class
 */
public class ToggleNBTTooltipCommand extends CommandBase {

    /**
     * Always returns true
     *
     * @param sender This parameter is just there for the Override
     * @return true
     */
    @Override
    public boolean canCommandSenderUseCommand ( final ICommandSender sender ) { return true; }

    /**
     * Returns the name of the command
     *
     * @return "togglenbttooltip"
     */
    @Override
    public String getCommandName () { return "togglenbttooltip"; }

    /**
     * Returns the usage for this command
     *
     * @param sender This parameter is just there for the Override
     * @return "togglenbttooltip"
     */
    @Override
    public String getCommandUsage ( final ICommandSender sender ) { return getCommandName (); }

    /**
     * Called when /togglenbttooltip is used
     *
     * @param sender The sender of the command
     * @param args The arguments of the command
     */
    @Override
    public void processCommand ( final ICommandSender sender , final String [] args ) {

        // Errors
        if ( args.length != 0 ) {

            sender.addChatMessage ( TextUtils.formatText ( ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage ( sender ) ) );
            return;

        }

        // Getting the old value and toggling it
        final boolean value = ! ConfigurationManager.isShowingNBTTooltip ();

        // Setting the value
        ConfigurationManager.showNBTTooltip ( value );

        // Sending a confirmation message
        sender.addChatMessage ( TextUtils.formatText ( ChatColor.GREEN + "Toggled " + ( ( value ) ? "on" : "off" ) + " the NBT in item tooltips!" ) );

    }

}
