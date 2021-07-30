package me.doclic.minecraft.mods.addons.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Class for KeyBindings, instantiated in {@link me.doclic.minecraft.mods.addons.DoclicAddonsMod#DoclicAddonsMod()}
 */
public class KeyBindings {

    /**
     * The {@link KeyBinding} to disable writing Packets
     */
    public static final KeyBinding STOP_PACKET = new KeyBinding("Stop", Keyboard.KEY_G, "Sending Packets");
    /**
     * The {@link KeyBinding} to enable writing Packets
     */
    public static final KeyBinding START_PACKET = new KeyBinding("Start", Keyboard.KEY_H, "Sending Packets");
    /**
     * Preventing multiple instances
     */
    private static boolean alreadyCreated = false;

    /**
     * Registers the KeyBindings
     */
    public KeyBindings() {

        // Preventing multiple instances
        if (alreadyCreated) return;
        alreadyCreated = true;

        // Registering the Key Bindings
        ClientRegistry.registerKeyBinding(STOP_PACKET);
        ClientRegistry.registerKeyBinding(START_PACKET);

        // Registering this instance to the EventBus
        MinecraftForge.EVENT_BUS.register(this);

    }

    /**
     * Listener for {@link InputEvent.KeyInputEvent}
     *
     * @param e The event
     */
    @SubscribeEvent
    public void onEvent(final InputEvent.KeyInputEvent e) {

        if (STOP_PACKET.isPressed()) {

            PacketUtils.disableWriting();
            return;

        } if (START_PACKET.isPressed()) {

            PacketUtils.enableWriting();

        }

    }

}
