package me.wiceh.companies.listeners;

import me.wiceh.companies.Companies;
import me.wiceh.companies.inventories.InsertProductsInventory;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class InventoryInteractListener implements Listener {

    private final Companies plugin;

    public InventoryInteractListener(Companies plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!plugin.getCompanyMap().containsKey(player)) return;
        Company company = plugin.getCompanyMap().get(player);

        if (event.getView().getTitle().startsWith(Utils.toSmallText("cassa") + " (")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(plugin, "player");
            if (!meta.getPersistentDataContainer().has(key)) return;

            String playerUuid = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (playerUuid == null) return;

            Player p = Bukkit.getPlayer(UUID.fromString(playerUuid));
            if (p == null) {
                player.sendMessage("§cQuesto cittadino non è in città.");
                return;
            }

            new InsertProductsInventory(plugin).open(player, company, p);
        }
    }
}
