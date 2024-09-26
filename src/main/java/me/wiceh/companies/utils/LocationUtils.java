package me.wiceh.companies.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static String locationToString(Location location) {
        return location.getWorld().getName() + "|" + location.getX() + "|" + location.getY() + "|" + location.getZ();
    }

    public static Location locationFromString(String string) {
        String[] args = string.split("\\|");
        return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
    }
}
