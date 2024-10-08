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

public class InsertPriceInventory {

    private final Companies plugin;

    public InsertPriceInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Company company, Player target, String products) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (Utils.isInteger(text)) {
                        int price = Integer.parseInt(text);
                        plugin.getReceiptUtils().makeReceipt(player, company, target, products, price);
                    } else {
                        Utils.sendMessage(player, Icon.ERROR_YELLOW, "Inserisci un numero valido.");
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .text("Prezzo")
                .title("Inserisci il prezzo")
                .itemLeft(ItemUtils.getInvisibleItem())
                .plugin(plugin)
                .open(player);
    }
}
