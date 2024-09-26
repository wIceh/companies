package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class CashRegisterInventory {

    private final Companies plugin;

    public CashRegisterInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Company company) {
        Inventory inventory = Bukkit.createInventory(null, 27, text("§fCassa §l" + company.getName().toUpperCase()));

        Utils.setFiller1(inventory);

        ItemStack creaScontrino = new ItemStack(Material.PAPER);
        ItemMeta creaScontrinoMeta = creaScontrino.getItemMeta();
        creaScontrinoMeta.setDisplayName("§6Crea §lScontrino");
        creaScontrinoMeta.setCustomModelData(10392);
        creaScontrinoMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "crea-scontrino");
        creaScontrino.setItemMeta(creaScontrinoMeta);

        ItemStack ottieniPos = new ItemStack(Material.PAPER);
        ItemMeta ottieniPosMeta = ottieniPos.getItemMeta();
        ottieniPosMeta.setDisplayName("§7Ottieni §lPOS");

        // todo: implement pos, remove this lore :D
        ottieniPosMeta.setLore(List.of("§c» Non disponibile"));

        ottieniPosMeta.setCustomModelData(10372);
        ottieniPosMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "ottieni-pos");
        ottieniPos.setItemMeta(ottieniPosMeta);

        ItemStack listaScontrini = new ItemStack(Material.PAPER);
        ItemMeta listaScontriniMeta = listaScontrini.getItemMeta();
        listaScontriniMeta.setDisplayName("§bLista §lScontrini");
        listaScontriniMeta.setCustomModelData(10284);
        listaScontriniMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "lista-scontrini");
        listaScontrini.setItemMeta(listaScontriniMeta);

        ItemStack annuncioPubblicitario = new ItemStack(Material.PAPER);
        ItemMeta annuncioPubblicitarioMeta = annuncioPubblicitario.getItemMeta();
        annuncioPubblicitarioMeta.setDisplayName("§9Manda un annuncio §lPubblicitario");
        annuncioPubblicitarioMeta.setCustomModelData(10286);
        annuncioPubblicitarioMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "item"), PersistentDataType.STRING, "annuncio-pubblicitario");
        annuncioPubblicitario.setItemMeta(annuncioPubblicitarioMeta);

        inventory.setItem(10, creaScontrino);
        inventory.setItem(12, ottieniPos);
        inventory.setItem(14, listaScontrini);
        inventory.setItem(16, annuncioPubblicitario);

        player.openInventory(inventory);
        plugin.getCompanyMap().put(player, company);
    }
}
