package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Role;
import me.wiceh.companies.utils.ItemUtils;
import me.wiceh.companies.utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class DelRoleInventory {

    private final Companies plugin;

    public DelRoleInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Company company) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (!text.trim().isEmpty()) {
                        Optional<Role> role = plugin.getRoleUtils().getRole(text, company);
                        if (role.isEmpty()) {
                            player.sendMessage(text("§cRuolo non trovato."));
                            return List.of(AnvilGUI.ResponseAction.close());
                        }

                        plugin.getRoleUtils().removeRole(role.get()).thenAccept(result -> {
                            if (result) {
                                player.sendMessage(text("§aRuolo eliminato con successo."));
                                return;
                            }
                            player.sendMessage(text("§cErrore."));
                        });
                        return List.of(AnvilGUI.ResponseAction.close());
                    } else {
                        player.sendMessage(text("§cInserisci il nome del ruolo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }
                })
                .text(Utils.toSmallText("inserisci nome"))
                .title("Nome")
                .itemLeft(ItemUtils.getInvisibleItem())
                .plugin(plugin)
                .open(player);
    }
}
