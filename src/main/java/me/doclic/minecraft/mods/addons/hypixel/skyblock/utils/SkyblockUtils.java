package me.doclic.minecraft.mods.addons.hypixel.skyblock.utils;

import me.doclic.minecraft.mods.addons.utils.NetworkingUtils;

public class SkyblockUtils {

    public static String getCookieOwner(final String ownerUUID) {

        if (!ownerUUID.isEmpty())
            return NetworkingUtils.getNameFromUUID(ownerUUID);

        return null;

    }

}
