package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * /light Command class
 */
public class LightCommand extends CommandBase {

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
     * @return "light"
     */
    @Override
    public String getCommandName () { return "light"; }

    /**
     * Returns the usage for this command
     *
     * @param sender This parameter is just there for the Override
     * @return "light"
     */
    @Override
    public String getCommandUsage ( final ICommandSender sender ) { return getCommandName (); }

    /**
     * Called when /light is used
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

        // Setting the value
        Minecraft.getMinecraft ().gameSettings.gammaSetting = 69420;

        // Sending a confirmation message
        sender.addChatMessage ( TextUtils.formatText ( ChatColor.GREEN + "Granted you night vision! (This works by setting your brightness value to a number way over the limit)" ) );

    }

}
