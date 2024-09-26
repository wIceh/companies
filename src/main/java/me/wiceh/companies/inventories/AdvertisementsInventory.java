package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static net.kyori.adventure.text.Component.text;

public class AdvertisementsInventory {

    private final Companies plugin;

    public AdvertisementsInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, text("§fAnnunci §lPubblicitari"));

        Utils.setFiller2(inventory);

        ItemStack apriSede = new ItemStack(Material.PAPER);
        ItemMeta apriSedeMeta = apriSede.getItemMeta();
        apriSedeMeta.setDisplayName("§aApri la §lSede");
        apriSedeMeta.setCustomModelData(10396);
        apriSedeMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "apri-sede");
        apriSede.setItemMeta(apriSedeMeta);

        ItemStack chiudiSede = new ItemStack(Material.PAPER);
        ItemMeta chiudiSedeMeta = chiudiSede.getItemMeta();
        chiudiSedeMeta.setDisplayName("§cChiudi la §lSede");
        chiudiSedeMeta.setCustomModelData(10397);
        chiudiSedeMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "chiudi-sede");
        chiudiSede.setItemMeta(chiudiSedeMeta);

        ItemStack chiusuraMomentanea = new ItemStack(Material.PAPER);
        ItemMeta chiusuraMomentaneaMeta = chiusuraMomentanea.getItemMeta();
        chiusuraMomentaneaMeta.setDisplayName("§6Chiudi momentaneamente la §lSede");
        chiusuraMomentaneaMeta.setCustomModelData(10364);
        chiusuraMomentaneaMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "chiusura-temporanea");
        chiusuraMomentanea.setItemMeta(chiusuraMomentaneaMeta);

        ItemStack tornaIndietro = new ItemStack(Material.PAPER);
        ItemMeta tornaIndietroMeta = tornaIndietro.getItemMeta();
        tornaIndietroMeta.setDisplayName("§cTorna §lIndietro");
        tornaIndietroMeta.setCustomModelData(10252);
        tornaIndietroMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "torna-indietro");
        tornaIndietro.setItemMeta(tornaIndietroMeta);

        inventory.setItem(11, apriSede);
        inventory.setItem(13, chiudiSede);
        inventory.setItem(15, chiusuraMomentanea);
        inventory.setItem(18, tornaIndietro);

        player.openInventory(inventory);
    }
}
