package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.RoleType;
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
                        Optional<Role> role = plugin.getRoleUtils().getRole(text, company);
                        if (role.isPresent()) {
                            player.sendMessage(text("§cEsiste già questo ruolo in quest'azienda."));
                            return List.of(AnvilGUI.ResponseAction.close());
                        }
                        open2(player, company, text);
                    } else {
                        player.sendMessage(text("§cInserisci il nome del ruolo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .text(Utils.toSmallText("inserisci nome"))
                .title("Nome")
                .itemLeft(ItemUtils.getInvisibleItem())
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
                .text(Utils.toSmallText("inserisci gruppo"))
                .title("Gruppo")
                .itemLeft(ItemUtils.getInvisibleItem())
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
                        if (isValidRoleType(text)) {
                            addRole(player, company, role, group, RoleType.valueOf(text.toUpperCase()));
                            return List.of(AnvilGUI.ResponseAction.close());
                        } else {
                            player.sendMessage(text("§cTipo non valido.."));
                            return List.of(AnvilGUI.ResponseAction.close());
                        }
                    } else {
                        player.sendMessage(text("§cInserisci il tipo del ruolo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }
                })
                .text(Utils.toSmallText("inserisci tipo"))
                .title("Tipo")
                .itemLeft(ItemUtils.getInvisibleItem())
                .plugin(plugin)
                .open(player);
    }

    public void addRole(Player player, Company company, String role, String group, RoleType type) {
        plugin.getRoleUtils().addRole(company, role, group, type).thenAccept(result -> {
            if (result) {
                player.sendMessage(text("§aRuolo aggiunto con successo."));
                return;
            }
            player.sendMessage(text("§cErrore."));
        });
    }

    private boolean isValidRoleType(String string) {
        try {
            RoleType.valueOf(string.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
