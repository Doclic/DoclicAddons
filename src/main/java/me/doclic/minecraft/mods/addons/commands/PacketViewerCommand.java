package me.doclic.minecraft.mods.addons.commands;

import me.doclic.minecraft.mods.addons.utils.ChatColor;
import me.doclic.minecraft.mods.addons.utils.PacketUtils;
import me.doclic.minecraft.mods.addons.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

/**
 * /packetviewer Command class
 */
public class PacketViewerCommand extends CommandBase {

    final PacketUtils.CustomPacketHandler packetHandler = new PacketUtils.CustomPacketHandler() {

        @Override
        public String getName() { return "packetviewer"; }

        @Override
        public boolean onWrite(Object packet) {

            Minecraft.getMinecraft().thePlayer.addChatMessage(TextUtils.formatText(
                    ChatColor.LIGHT_PURPLE + "[Serverbound] " + ChatColor.DARK_PURPLE +
                            PacketUtils.packetToString((Packet<? extends INetHandler>) packet)));

            return true;

        }

        @Override
        public boolean onRead(Object packet) {

            /*Minecraft.getMinecraft().thePlayer.addChatMessage(*/TextUtils.formatText(
                    ChatColor.LIGHT_PURPLE + "[Clientbound]   " + ChatColor.DARK_PURPLE +
                            PacketUtils.packetToString((Packet<? extends INetHandler>) packet))/*)*/;

            return true;

        }

    };

    /**
     * Always returns true
     *
     * @param sender This parameter is just there for the Override
     * @return true
     */
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return true;
    }

    /**
     * Returns the name of the command
     *
     * @return "packetviewer"
     */
    @Override
    public String getCommandName() {
        return "packetviewer";
    }

    /**
     * Returns the usage for this command
     *
     * @param sender This parameter is just there for the Override
     * @return "packetviewer"
     */
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return getCommandName();
    }

    /**
     * Called when /packetviewer is used
     *
     * @param sender The sender of the command
     * @param args   The arguments of the command
     */
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {

        // Errors
        if (args.length != 0) {

            sender.addChatMessage(TextUtils.formatText(ChatColor.RED + "Too many arguments!\n" + ChatColor.RED + "Syntax: " + getCommandUsage(sender)));
            return;

        }

        // Toggling the Packet Handler
        final boolean alreadyRegistered = PacketUtils.isPacketHandlerRegistered(packetHandler);
        if (alreadyRegistered) PacketUtils.removePacketHandler(packetHandler);
        else PacketUtils.addPacketHandler(packetHandler);

        // Sending a confirmation message
        sender.addChatMessage(TextUtils.formatText(ChatColor.GREEN + "Toggled " + (alreadyRegistered ? "off" : "on") + " the Packet viewer!"));

    }

}
