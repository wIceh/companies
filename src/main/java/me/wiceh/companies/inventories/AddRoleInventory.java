package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.RoleType;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class AddRoleInventory {

    private final Companies plugin;

    public AddRoleInventory(Companies plugin) {
        this.plugin = plugin;
    }

    public void open1(Player player, Company company) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (!text.trim().isEmpty()) {
                        open2(player, company, text);
                    } else {
                        player.sendMessage(text("§cInserisci il nome del ruolo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return Collections.emptyList();
                })
                .text(Utils.toSmallText("ruolo"))
                .title("Come lo vuoi chiamare il ruolo?")
                .plugin(plugin)
                .open(player);
    }

    public void open2(Player player, Company company, String role) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (!text.trim().isEmpty()) {
                        open3(player, company, role, text.toLowerCase());
                    } else {
                        player.sendMessage(text("§cInserisci il nome del gruppo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return Collections.emptyList();
                })
                .text(Utils.toSmallText("gruppo"))
                .title("Inserisci il nome del gruppo")
                .plugin(plugin)
                .open(player);
    }

    public void open3(Player player, Company company, String role, String group) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (!text.trim().isEmpty()) {
                        createRole(player, company, role, group, RoleType.valueOf(text.toUpperCase()));
                    } else {
                        player.sendMessage(text("§cInserisci il tipo di ruolo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return Collections.emptyList();
                })
                .text(Utils.toSmallText("tipo"))
                .title("Inserisci il tipo di ruolo")
                .plugin(plugin)
                .open(player);
    }

    public void createRole(Player player, Company company, String role, String group, RoleType type) {
        plugin.getRoleUtils().addRole(company, role, group, type).thenAccept(result -> {
            if (result.isPresent()) {
                player.sendMessage(text("§aRuolo creato con successo."));
                return;
            }
            player.sendMessage(text("§cErrore."));
        });
    }
}
