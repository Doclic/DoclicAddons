package me.doclic.minecraft.mods.addons.utils;

import me.doclic.minecraft.mods.addons.DoclicAddonsMod;
import io.netty.channel.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import java.util.LinkedList;

/**
 * Utilities for packets
 */
public class PacketUtils {

    /**
     * The name for the Packet Disabler {@link ChannelDuplexHandler}
     */
    private static final String packetDisablerName = "packet_disabler";
    /**
     * The queued outgoing (Server bound) packets
     * Check https://web.archive.org/web/20151222133335/https://wiki.vg/Protocol for more information
     */
    private static final LinkedList<Packet<? extends INetHandler>> queuedPackets = new LinkedList<Packet<? extends INetHandler>>();

    /**
     * This function stops disable sending Packets to the server
     * They are still queued and will be all sent when {@link #enableWriting()} is called
     */
    public static void disableWriting() {

        // Getting the Minecraft instance
        final Minecraft minecraft = Minecraft.getMinecraft();

        // Creating the Packet Handler
        final ChannelDuplexHandler packetHandler = new ChannelDuplexHandler() {

            @Override
            public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) {

                queuedPackets.add((Packet<? extends INetHandler>) packet);

            }

        };

        // Registering the Packet Handler to the pipeline
        final ChannelPipeline pipeline = minecraft.getNetHandler().getNetworkManager().channel().pipeline();
        /// Preventing from registering twice which would crash the game
        if (pipeline.get(packetDisablerName) != null) {

            minecraft.thePlayer.addChatMessage(TextUtils.formatText(ChatColor.RED + "Packet writing was already stopped."));
            return;

        }
        /// Actually register the Custom Packet Handler
        pipeline.addBefore("packet_handler", packetDisablerName, packetHandler);

        // Info
        DoclicAddonsMod.LOGGER.info("Stopped packet writing");
        minecraft.thePlayer.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "Stopped packet writing"));

    }

    /**
     * This function enables writing Packets to the server
     * It also sends every queued Packets
     */
    public static void enableWriting() {

        // Getting variables
        final Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayerSP player = minecraft.thePlayer;
        final NetHandlerPlayClient networkHandler = minecraft.getNetHandler();
        final Channel channel = networkHandler.getNetworkManager().channel();

        // Checking if the Custom Packet Handler is in the Pipeline
        if (channel.pipeline().get(packetDisablerName) == null) {

            player.addChatMessage(TextUtils.formatText(ChatColor.RED + "Packet writing is already enabled!"));
            return;

        }

        // Removing the Packet Handler from the Pipeline
        channel.eventLoop().submit(new Runnable() {
            @Override
            public void run() { channel.pipeline().remove(packetDisablerName); }
        });

        // Info
        DoclicAddonsMod.LOGGER.info("Restarted packet writing");
        player.addChatMessage(TextUtils.formatText(ChatColor.BLUE + "Restarted packet writing"));

        // Sending the Packets
        final int size = queuedPackets.size();
        for (int i = 0; i < size; i++) {

            networkHandler.addToSendQueue(queuedPackets.getFirst());
            queuedPackets.removeFirst();

        }

    }

    /**
     * Sends a Packet to the server
     *
     * @param packet The Packet you're sending
     */
    public static void sendPacket(Packet<? extends INetHandler> packet) {

        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);

    }

}
