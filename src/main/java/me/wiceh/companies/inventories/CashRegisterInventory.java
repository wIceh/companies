package me.wiceh.companies.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;

public class CashRegisterInventory {

    private final Companies plugin;

    public CashRegisterInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open1(Player player, Company company) {
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

        // todo: loop and add all players heads

        gui.open(player);
        plugin.getCompanyMap().put(player, company);
    }
}
