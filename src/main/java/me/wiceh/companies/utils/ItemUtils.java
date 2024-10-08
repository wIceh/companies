package me.wiceh.companies.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static ItemStack getFiller() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§f");
        meta.setCustomModelData(10266);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getOutputItem() {
        ItemBuilder item = ItemBuilder.from(Material.PAPER)
                .model(10258);

        return item.build();
    }

    public static ItemStack getInvisibleItem() {
        ItemBuilder item = ItemBuilder.from(Material.PAPER)
                .model(4);

        return item.build();
    }
}
