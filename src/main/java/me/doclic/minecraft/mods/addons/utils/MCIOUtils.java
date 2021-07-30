package me.doclic.minecraft.mods.addons.utils;

import com.google.gson.JsonObject;
import me.doclic.minecraft.mods.addons.DoclicAddonsMod;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utilities for .minecraft directory
 */
public class MCIOUtils {

    /**
     * The name of the storage directory
     */
    public static final String STORAGE_DIR_NAME = DoclicAddonsMod.MOD_ID;

    /**
     * The path to the .minecraft directory
     */
    public static final String MC_PATH = Loader.instance().getConfigDir().getParentFile().getAbsolutePath();
    /**
     * The path to the .minecraft/{@link DoclicAddonsMod#MOD_ID}
     */
    public static final String STORAGE_PATH = MC_PATH + File.separatorChar + STORAGE_DIR_NAME;

    /**
     * Creates all files needed
     *
     * @throws IOException if the storage directory, the readme.txt file or the ah_bin.json file was unable to be created or if the readme.txt file or the ah_bin.json file was unable to be set writable
     */
    public static void initiate() throws IOException {

        final File storage = new File(MC_PATH, STORAGE_DIR_NAME);
        DoclicAddonsMod.LOGGER.info(MC_PATH);
        DoclicAddonsMod.LOGGER.info(STORAGE_PATH);

        // Creates the storage directory (mkdir means MaKe DIRectory)
        if (!storage.exists() && !storage.mkdir())
            throw new IOException("Couldn't create the storage directory (path: " + storage.getAbsolutePath() + ")");

        // Creates the ahBin
        final File note = new File(STORAGE_PATH, "readme.txt");
        if (!note.exists() && !note.createNewFile())
            throw new IOException("Couldn't create the " + note.getName() + "file.");

        /// Writing
        if (!note.setWritable(true))
            throw new IOException("Couldn't make the " + note.getName() + " file writable.");
        final FileWriter noteFileWriter = new FileWriter(note);
        noteFileWriter.write(
                "Welcome to the Doclic Addons' storage directory!\n" +
                        "Here is a quick explanation of what every file does:\n\n" +

                        "ah_bin.json: This file contains every bin auctions Auction Bot found. This is used to get the average\n" +
                        "price of every item.\n\n" +

                        "You probably also noticed that there are some folders/directories in here too.\n" +
                        "Since most of them don't have a readme file, here's an explanation of what every directory contains:\n\n" +

                        "scripts: currently useless, see in later version"
        );
        noteFileWriter.close();

        // Creates the ah_bin.json file
        final File ahBin = new File(STORAGE_PATH, "ah_bin.json");
        if (!ahBin.exists() && !ahBin.createNewFile())
            throw new IOException("Couldn't create the " + ahBin.getName() + "file.");

        /// Writing
        if (!ahBin.setWritable(true))
            throw new IOException("Couldn't make the " + ahBin.getName() + " file writable.");
        final FileWriter ahBinFileWriter = new FileWriter(ahBin);
        ahBinFileWriter.write(new JsonObject().toString());
        ahBinFileWriter.close();
    }

}
