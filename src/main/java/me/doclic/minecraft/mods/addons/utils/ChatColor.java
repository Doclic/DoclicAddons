package me.doclic.minecraft.mods.addons.utils;

// https://minecraft.tools/en/color-code.php

/**
 * Modified code originally made by md_5
 */
public enum ChatColor {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),

    RESET('r'),
    BOLD('l'),
    ITALIC('o'),
    UNDERLINED('n'),
    STRIKETHROUGH('m'),
    OBFUSCATED('k');

    /**
     * Caching {@link #toString()} return value
     */
    final String string;

    /**
     * Creates a ChatColor with the provided color code
     *
     * @param code the code for this color
     */
    ChatColor(char code) { string = "\u00A7" + code; }

    /**
     * Returns a String that will change the color of the following text
     *
     * @return a String that will change the color of the following text
     */
    @Override
    public String toString() { return string; }

}
