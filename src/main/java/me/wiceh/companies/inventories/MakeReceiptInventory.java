package me.wiceh.companies.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class MakeReceiptInventory {

    private final Companies plugin;

    public MakeReceiptInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Company company) {
        PaginatedGui gui = Gui.paginated()
                .title(text("§fCrea uno §lScontrino"))
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

        gui.setItem(18, back.asGuiItem(action -> new CashRegisterInventory(plugin).open(player, company)));
        gui.setItem(19, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(20, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(21, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(22, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(23, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(24, ItemBuilder.from(ItemUtils.getFiller()).asGuiItem());
        gui.setItem(25, previousPage.asGuiItem(action -> gui.previous()));
        gui.setItem(26, nextPage.asGuiItem(action -> gui.next()));

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(player.getLocation()) > 5) continue;

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setDisplayName("§2Avvia la transazione");
            meta.setLore(List.of("§aCliente §8> §f" + p.getName(), "§aMetodo di pagamento §8> §fContanti", "§7» Clicca per continuare"));
            meta.setOwningPlayer(p);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "player"), PersistentDataType.STRING, p.getUniqueId().toString());
            item.setItemMeta(meta);

            gui.addItem(ItemBuilder.from(item).asGuiItem());
        }

        gui.open(player);
    }
}
