package me.doclic.minecraft.mods.addons;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class ConfigurationManager {

    private static Configuration configuration = null;

    // Variables from CONFIGURATION
    private static Property allowCopy;
    private static Property showNBTTooltip;
    private static Property nbtTooltipMaxLength;
    private static Property apiKey;

    // Getter Methods
    public static boolean isAllowingCopy() { return allowCopy.getBoolean(); }
    public static boolean isShowingNBTTooltip() { return showNBTTooltip.getBoolean(); }
    public static int getNBTTooltipMaxLength() { return nbtTooltipMaxLength.getInt(); }
    public static String getAPIKey() { return apiKey.getString(); }

    // Setter Methods
    public static void allowCopy(final boolean allowCopy) {

        // Sets the value in the Configuration
        ConfigurationManager.allowCopy.set(allowCopy);

        // Saves the Configuration
        if (configuration.hasChanged())
            configuration.save();

    }

    public static void showNBTTooltip(final boolean showNBTTooltip) {

        // Sets the value in the Configuration
        ConfigurationManager.showNBTTooltip.set(showNBTTooltip);

        // Saves the Configuration
        if (configuration.hasChanged())
            configuration.save();

    }

    public static void setNbtTooltipMaxLength(final int nbtTooltipMaxLength) {

        // Sets the value in the Configuration
        ConfigurationManager.nbtTooltipMaxLength.set(Math.max(1, nbtTooltipMaxLength));

        // Saves the Configuration
        if (configuration.hasChanged())
            configuration.save();

    }

    public static void setApiKey(String apiKey) {

        // Sets the value in the Configuration
        ConfigurationManager.apiKey.set(apiKey);

        // Saves the Configuration
        if (configuration.hasChanged())
            configuration.save();

    }

    // Setting variables
    public static void createVariables() {

        // If the Configuration has already been created, return
        if (configuration != null) return;

        // Getting the Configuration
        configuration = new Configuration(new File(Loader.instance().getConfigDir(), "DoclicAddons.cfg"));
        configuration.load();

        // Sets the values (in Configuration if not already and in class)
        allowCopy = configuration.get("nbt", "allowCopy", false);
        showNBTTooltip = configuration.get("nbt", "showNBTTooltip", true);
        nbtTooltipMaxLength = configuration.get("nbt", "nbtTooltipMaxLength", 200);
        apiKey = configuration.get("hypixel", "apikey", "not_set");

        // Saves the Configuration
        if (configuration.hasChanged())
            configuration.save();

    }

}
