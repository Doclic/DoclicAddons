package me.doclic.minecraft.mods.addons.utils;

import net.minecraft.util.IChatComponent;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Utilities for text
 */
public class TextUtils {

    /**
     * Formats text for it to be sent in chat
     *
     * @param text The unformatted text
     * @return The formatted text for chat use
     */
    public static IChatComponent formatText(final String text) {

        // Formats the text
        return IChatComponent.Serializer.jsonToComponent("{\"text\":\"" + text + "\"}");

    }

    /**
     * Copies a String to the clipboard
     *
     * @param clipboard The String that will be copied
     * @return true if the String was not already in the clipboard, false otherwise
     */
    public static boolean copyToClipboard(final String clipboard) {

        // Copies the value to clipboard
        final StringSelection selection = new StringSelection(clipboard);
        final Clipboard clipboardInstance = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Is the content already in Clipboard
        try {

            final Object value = clipboardInstance.getContents(null).getTransferData(DataFlavor.stringFlavor);
            if (clipboard.equals(value)) return false;

        } catch (IOException e) { e.printStackTrace(); }
        catch (UnsupportedFlavorException e) { e.printStackTrace(); }

        // Copying to Clipboard
        clipboardInstance.setContents(selection, selection);

        return true;

    }

}
