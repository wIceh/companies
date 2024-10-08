package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.Icon;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.ItemUtils;
import me.wiceh.companies.utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class InsertProductsInventory {

    private final Companies plugin;

    public InsertProductsInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Company company, Player target) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (text.trim().length() > 1) {
                        new InsertPriceInventory(plugin).open(player, company, target, text);
                    } else {
                        Utils.sendMessage(player, Icon.ERROR_YELLOW, "Inserisci i prodotti.");
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return Collections.emptyList();
                })
                .text("Prodotti")
                .title("Inserisci i prodotti")
                .itemLeft(ItemUtils.getInvisibleItem())
                .plugin(plugin)
                .open(player);
    }
}
