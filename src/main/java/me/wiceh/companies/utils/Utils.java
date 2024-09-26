package me.wiceh.companies.utils;

import me.wiceh.companies.constants.Icon;
import me.wiceh.companies.constants.Palette;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.text;

public class Utils {

    private static final Map<Character, Character> SMALL_TEXT_MAP = new HashMap<>();

    static {
        SMALL_TEXT_MAP.put('a', 'ᴀ');
        SMALL_TEXT_MAP.put('b', 'ʙ');
        SMALL_TEXT_MAP.put('c', 'ᴄ');
        SMALL_TEXT_MAP.put('d', 'ᴅ');
        SMALL_TEXT_MAP.put('e', 'ᴇ');
        SMALL_TEXT_MAP.put('f', 'ꜰ');
        SMALL_TEXT_MAP.put('g', 'ɢ');
        SMALL_TEXT_MAP.put('h', 'ʜ');
        SMALL_TEXT_MAP.put('i', 'ɪ');
        SMALL_TEXT_MAP.put('j', 'ᴊ');
        SMALL_TEXT_MAP.put('k', 'ᴋ');
        SMALL_TEXT_MAP.put('l', 'ʟ');
        SMALL_TEXT_MAP.put('m', 'ᴍ');
        SMALL_TEXT_MAP.put('n', 'ɴ');
        SMALL_TEXT_MAP.put('o', 'ᴏ');
        SMALL_TEXT_MAP.put('p', 'ᴘ');
        SMALL_TEXT_MAP.put('q', 'ǫ');
        SMALL_TEXT_MAP.put('r', 'ʀ');
        SMALL_TEXT_MAP.put('s', 'ѕ');
        SMALL_TEXT_MAP.put('t', 'ᴛ');
        SMALL_TEXT_MAP.put('u', 'ᴜ');
        SMALL_TEXT_MAP.put('v', 'ᴠ');
        SMALL_TEXT_MAP.put('w', 'ᴡ');
        SMALL_TEXT_MAP.put('x', 'х');
        SMALL_TEXT_MAP.put('y', 'ʏ');
        SMALL_TEXT_MAP.put('z', 'ᴢ');

        SMALL_TEXT_MAP.put('A', 'ᴀ');
        SMALL_TEXT_MAP.put('B', 'ʙ');
        SMALL_TEXT_MAP.put('C', 'ᴄ');
        SMALL_TEXT_MAP.put('D', 'ᴅ');
        SMALL_TEXT_MAP.put('E', 'ᴇ');
        SMALL_TEXT_MAP.put('F', 'ꜰ');
        SMALL_TEXT_MAP.put('G', 'ɢ');
        SMALL_TEXT_MAP.put('H', 'ʜ');
        SMALL_TEXT_MAP.put('I', 'ɪ');
        SMALL_TEXT_MAP.put('J', 'ᴊ');
        SMALL_TEXT_MAP.put('K', 'ᴋ');
        SMALL_TEXT_MAP.put('L', 'ʟ');
        SMALL_TEXT_MAP.put('M', 'ᴍ');
        SMALL_TEXT_MAP.put('N', 'ɴ');
        SMALL_TEXT_MAP.put('O', 'ᴏ');
        SMALL_TEXT_MAP.put('P', 'ᴘ');
        SMALL_TEXT_MAP.put('Q', 'ǫ');
        SMALL_TEXT_MAP.put('R', 'ʀ');
        SMALL_TEXT_MAP.put('S', 'ѕ');
        SMALL_TEXT_MAP.put('T', 'ᴛ');
        SMALL_TEXT_MAP.put('U', 'ᴜ');
        SMALL_TEXT_MAP.put('V', 'ᴠ');
        SMALL_TEXT_MAP.put('W', 'ᴡ');
        SMALL_TEXT_MAP.put('X', 'х');
        SMALL_TEXT_MAP.put('Y', 'ʏ');
        SMALL_TEXT_MAP.put('Z', 'ᴢ');

        SMALL_TEXT_MAP.put('0', '₀');
        SMALL_TEXT_MAP.put('1', '₁');
        SMALL_TEXT_MAP.put('2', '₂');
        SMALL_TEXT_MAP.put('3', '₃');
        SMALL_TEXT_MAP.put('4', '₄');
        SMALL_TEXT_MAP.put('5', '₅');
        SMALL_TEXT_MAP.put('6', '₆');
        SMALL_TEXT_MAP.put('7', '₇');
        SMALL_TEXT_MAP.put('8', '₈');
        SMALL_TEXT_MAP.put('9', '₉');
    }

    public static String color(String text) {
        String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    public static void sendMessage(Player player, Icon icon, String message) {
        String hex = "";

        switch (icon) {
            case ERROR_BLUE -> hex = Palette.BLUE.getHex();
            case ERROR_GREEN -> hex = Palette.GREEN.getHex();
            case ERROR_RED -> hex = Palette.RED.getHex();
            case ERROR_YELLOW -> hex = Palette.YELLOW.getHex();
        }

        player.sendMessage(text(icon.getIcon() + " ").append(text(message).color(TextColor.fromHexString(hex))));
    }

    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(timeFormatter);
    }

    public static String formatDate(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return formatDate(localDateTime);
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String formatTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        StringBuilder stringBuilder = new StringBuilder();
        if (day > 0) {
            stringBuilder.append(day).append("d ");
        }
        if (hours > 0) {
            stringBuilder.append(hours).append("h ");
        }
        if (minutes > 0) {
            stringBuilder.append(minutes).append("m ");
        }
        stringBuilder.append(second).append("s");


        return stringBuilder.toString();
    }

    public static String format(String message) {
        return Utils.color(message.replace("\\n", "\n"));
    }

    public static String toSmallText(String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            result.append(SMALL_TEXT_MAP.getOrDefault(c, c));
        }

        return result.toString();
    }

    public static  void setFiller1(Inventory inventory) {
        if (inventory.getSize() == 27) {
            inventory.setItem(0, ItemUtils.getFiller());
            inventory.setItem(1, ItemUtils.getFiller());
            inventory.setItem(2, ItemUtils.getFiller());
            inventory.setItem(3, ItemUtils.getFiller());
            inventory.setItem(4, ItemUtils.getFiller());
            inventory.setItem(5, ItemUtils.getFiller());
            inventory.setItem(6, ItemUtils.getFiller());
            inventory.setItem(7, ItemUtils.getFiller());
            inventory.setItem(8, ItemUtils.getFiller());
            inventory.setItem(9, ItemUtils.getFiller());
            inventory.setItem(11, ItemUtils.getFiller());
            inventory.setItem(13, ItemUtils.getFiller());
            inventory.setItem(15, ItemUtils.getFiller());
            inventory.setItem(17, ItemUtils.getFiller());
            inventory.setItem(18, ItemUtils.getFiller());
            inventory.setItem(19, ItemUtils.getFiller());
            inventory.setItem(20, ItemUtils.getFiller());
            inventory.setItem(21, ItemUtils.getFiller());
            inventory.setItem(22, ItemUtils.getFiller());
            inventory.setItem(23, ItemUtils.getFiller());
            inventory.setItem(24, ItemUtils.getFiller());
            inventory.setItem(25, ItemUtils.getFiller());
            inventory.setItem(26, ItemUtils.getFiller());
        }
    }

    public static void setFiller2(Inventory inventory) {
        if (inventory.getSize() == 27) {
            inventory.setItem(0, ItemUtils.getFiller());
            inventory.setItem(1, ItemUtils.getFiller());
            inventory.setItem(2, ItemUtils.getFiller());
            inventory.setItem(3, ItemUtils.getFiller());
            inventory.setItem(4, ItemUtils.getFiller());
            inventory.setItem(5, ItemUtils.getFiller());
            inventory.setItem(6, ItemUtils.getFiller());
            inventory.setItem(7, ItemUtils.getFiller());
            inventory.setItem(8, ItemUtils.getFiller());
            inventory.setItem(9, ItemUtils.getFiller());
            inventory.setItem(10, ItemUtils.getFiller());
            inventory.setItem(12, ItemUtils.getFiller());
            inventory.setItem(14, ItemUtils.getFiller());
            inventory.setItem(16, ItemUtils.getFiller());
            inventory.setItem(17, ItemUtils.getFiller());
            inventory.setItem(18, ItemUtils.getFiller());
            inventory.setItem(19, ItemUtils.getFiller());
            inventory.setItem(20, ItemUtils.getFiller());
            inventory.setItem(21, ItemUtils.getFiller());
            inventory.setItem(22, ItemUtils.getFiller());
            inventory.setItem(23, ItemUtils.getFiller());
            inventory.setItem(24, ItemUtils.getFiller());
            inventory.setItem(25, ItemUtils.getFiller());
            inventory.setItem(26, ItemUtils.getFiller());
        }
    }
}
