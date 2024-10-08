package me.wiceh.companies.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Receipt;
import me.wiceh.companies.utils.ItemUtils;
import me.wiceh.companies.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class ReceiptsListInventory {

    private final Companies plugin;

    public ReceiptsListInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Company company) {
        PaginatedGui gui = Gui.paginated()
                .title(text("§fLista §lScontrini"))
                .rows(3)
                .pageSize(18)
                .create();

        ItemBuilder back = ItemBuilder.from(Material.PAPER)
                .name(text("§cTorna §lIndietro"))
                .model(10252);

        ItemBuilder previousPage = ItemBuilder.from(Material.PAPER)
                .name(text("§7Pagina §lPrecedente"))
                .model(10261);

        ItemBuilder nextPage = ItemBuilder.from(Material.PAPER)
                .name(text("§7Pagina §lSuccessiva"))
                .model(10263);

        gui.setItem(18, back.asGuiItem(action -> new CashRegisterInventory(plugin).open1(player, company)));
        gui.setItem(19, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(20, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(21, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(22, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(23, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(24, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(25, previousPage.asGuiItem(action -> gui.previous()));
        gui.setItem(26, nextPage.asGuiItem(action -> gui.next()));

        for (Receipt receipt : plugin.getReceiptUtils().getReceipts(company)) {
            ItemStack item = new ItemStack(Material.STICK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§aScontrino " + company.getName());
            meta.setLore(List.of("§7Cliente §8> §e" + receipt.getCustomer().getName(), "§7Cassiere §8> §e" + receipt.getCashier().getName(), "§7Prodotti §8> §e" + receipt.getProducts(), "§7Prezzo §8> §e" + receipt.getPrice() + "€", "§7Data §8> §e" + Utils.formatDate(receipt.getDate()), "", "§2ᴄʟɪᴄᴋ sɪɴɪsᴛʀᴏ ᴘᴇʀ ᴏᴛᴛᴇɴᴇʀᴇ ʟᴏ sᴄᴏɴᴛʀɪɴᴏ", "§4ᴄʟɪᴄᴋ ᴅᴇsᴛʀᴏ ᴘᴇʀ ʀɪᴍᴜᴏᴠᴇʀʟᴏ ᴅᴀʟʟᴀ ʟɪsᴛᴀ"));
            meta.setCustomModelData(10181);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "receiptId"), PersistentDataType.INTEGER, receipt.getId());
            item.setItemMeta(meta);

            gui.addItem(ItemBuilder.from(item).asGuiItem());
        }

        gui.open(player);
    }
}
