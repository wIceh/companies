package me.wiceh.companies.listeners;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.BroadcastType;
import me.wiceh.companies.constants.Icon;
import me.wiceh.companies.inventories.*;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Receipt;
import me.wiceh.companies.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
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

        if (ChatColor.stripColor(event.getView().getTitle()).startsWith("Cassa ")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(plugin, "item");
            if (!meta.getPersistentDataContainer().has(key)) return;

            String itemName = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (itemName == null) return;

            switch (itemName.toLowerCase()) {
                case "crea-scontrino":
                    new MakeReceiptInventory(plugin).open(player, company);
                    break;
                case "ottieni-pos":
                    if (!plugin.getCompanyUtils().isDirector(player, company)) {
                        Utils.sendMessage(player, Icon.ERROR_RED, "Non sei il direttore dell'azienda " + company.getName());
                        break;
                    }

                    break;
                case "lista-scontrini":
                    if (!plugin.getCompanyUtils().isDirector(player, company)) {
                        Utils.sendMessage(player, Icon.ERROR_RED, "Non sei il direttore dell'azienda " + company.getName());
                        break;
                    }

                    new ReceiptsListInventory(plugin).open(player, company);

                    break;
                case "annuncio-pubblicitario":
                    ConfigurationSection section = plugin.getConfig().getConfigurationSection("broadcasts." + plugin.getCompanyUtils().getCompany(company.getName()));
                    if (section == null) {
                        Utils.sendMessage(player, Icon.ERROR_RED, "Gli annunci pubblicitari di quest'azienda non sono configurati, contatta un amministratore.");
                        player.closeInventory();
                        return;
                    }

                    new AdvertisementsInventory(plugin).open(player);

                    break;
            }
        } else if (ChatColor.stripColor(event.getView().getTitle()).equals("Crea uno Scontrino")) {
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
                Utils.sendMessage(player, Icon.ERROR_RED, "Player non trovato.");
                return;
            }

            new InsertProductsInventory(plugin).open(player, company, p);
        } else if (ChatColor.stripColor(event.getView().getTitle()).equals("Lista Scontrini")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(plugin, "receiptId");
            if (!meta.getPersistentDataContainer().has(key)) return;

            int receiptId = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            Optional<Receipt> optionalReceipt = plugin.getReceiptUtils().getReceipt(receiptId);
            if (optionalReceipt.isEmpty()) return;

            Receipt receipt = optionalReceipt.get();

            if (event.getClick() == ClickType.LEFT) {
                player.getInventory().addItem(plugin.getReceiptUtils().getReceiptCopyItem(receipt));
            } else if (event.getClick() == ClickType.RIGHT) {
                plugin.getReceiptUtils().removeReceipt(receiptId);
                player.closeInventory();
                new ReceiptsListInventory(plugin).open(player, company);
            }
        } else if (ChatColor.stripColor(event.getView().getTitle()).equals("Annunci Pubblicitari")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(plugin, "item");
            if (!meta.getPersistentDataContainer().has(key)) return;

            String itemName = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (itemName == null) return;

            switch (itemName.toLowerCase()) {
                case "apri-sede":
                    plugin.getBroadcastUtils().sendBroadcast(player, company, BroadcastType.OPENING);
                    player.closeInventory();
                    break;
                case "chiudi-sede":
                    plugin.getBroadcastUtils().sendBroadcast(player, company, BroadcastType.CLOSING);
                    player.closeInventory();
                    break;
                case "chiusura-temporanea":
                    plugin.getBroadcastUtils().sendBroadcast(player, company, BroadcastType.TEMPORARY_CLOSURE);
                    player.closeInventory();
                    break;
                case "torna-indietro":
                    new CashRegisterInventory(plugin).open(player, company);
                    break;
            }
        }
    }
}
