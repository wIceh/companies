package me.wiceh.companies.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class CompaniesInventory {

    private final Companies plugin;

    public CompaniesInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(text("Lista Aziende"))
                .rows(4)
                .pageSize(27)
                .create();

        GuiItem glass = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(text("§f"))
                .asGuiItem();

        GuiItem previousPage = ItemBuilder.from(Material.PAPER)
                .name(text("§6" + Utils.toSmallText("pagina precedente")))
                .model(20)
                .asGuiItem(action -> gui.previous());

        GuiItem close = ItemBuilder.from(Material.PAPER)
                .name(text("§c" + Utils.toSmallText("chiudi")))
                .model(18)
                .asGuiItem(action -> gui.close(player));

        GuiItem nextPage = ItemBuilder.from(Material.PAPER)
                .name(text("§6" + Utils.toSmallText("pagina successiva")))
                .model(22)
                .asGuiItem(action -> gui.next());

        gui.setItem(27, glass);
        gui.setItem(28, glass);
        gui.setItem(29, glass);
        gui.setItem(30, previousPage);
        gui.setItem(31, close);
        gui.setItem(32, nextPage);
        gui.setItem(33, glass);
        gui.setItem(34, glass);
        gui.setItem(35, glass);

        List<Company> companies = plugin.getCompanyUtils().getCompanies();
        if (companies.isEmpty()) {
            player.sendMessage("§cNon ci sono aziende.");
            return;
        }

        for (Company c : companies) {
            OfflinePlayer director = Bukkit.getOfflinePlayer(UUID.fromString(c.getDirector()));

            ItemStack item = new ItemStack(Material.SPYGLASS);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§aAzienda #" + c.getName());
            meta.setLore(List.of(
                    "",
                    "§fDirettore: §7" + director.getName(),
                    "§fDipendenti: §7" + plugin.getEmployeeUtils().getEmployees(c).size(),
                    "§fGradi: §7" + plugin.getRoleUtils().getRoles(c).size(),
                    "§fBilancio: §7" + c.getBalance() + "€"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "company"), PersistentDataType.INTEGER, c.getId());
            item.setItemMeta(meta);

            gui.addItem(ItemBuilder.from(item).asGuiItem());
        }

        gui.open(player);
    }
}
