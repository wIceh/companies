package me.wiceh.companies.inventories;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Employee;
import me.wiceh.companies.objects.Role;
import me.wiceh.companies.utils.ItemUtils;
import me.wiceh.companies.utils.Utils;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class HireInventory {

    private final Companies plugin;

    public HireInventory(Companies plugin) {
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
                        Player target = Bukkit.getPlayerExact(text);
                        if (target == null) {
                            player.sendMessage(text("§cPlayer non trovato."));
                            return List.of(AnvilGUI.ResponseAction.close());
                        }

                        List<Employee> employees = plugin.getEmployeeUtils().getEmployees(company);
                        boolean isEmployee = employees.stream().anyMatch(employee -> employee.getPlayer().getUniqueId().toString().equals(target.getUniqueId().toString()));
                        if (isEmployee) {
                            player.sendMessage(text("§cQuesto player è già dipendente di quest'azienda."));
                            return List.of(AnvilGUI.ResponseAction.close());
                        }

                        open2(player, company, target);
                    } else {
                        player.sendMessage(text("§cInserisci il nome del cittadino."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .text(Utils.toSmallText("inserisci cittadino"))
                .title("Cittadino")
                .itemLeft(ItemUtils.getInvisibleItem())
                .plugin(plugin)
                .open(player);
    }

    public void open2(Player player, Company company, Player target) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (!text.trim().isEmpty()) {
                        Optional<Role> optionalRole = plugin.getRoleUtils().getRole(text, company);
                        if (optionalRole.isEmpty()) {
                            player.sendMessage(text("§cRuolo non trovato."));
                            return List.of(AnvilGUI.ResponseAction.close());
                        }

                        Role role = optionalRole.get();
                        sendHireRequest(player, company, target, role);
                        return List.of(AnvilGUI.ResponseAction.close());
                    } else {
                        player.sendMessage(text("§cInserisci il nome del ruolo."));
                        return List.of(AnvilGUI.ResponseAction.close());
                    }
                })
                .text(Utils.toSmallText("inserisci ruolo"))
                .title("Ruolo")
                .itemLeft(ItemUtils.getInvisibleItem())
                .plugin(plugin)
                .open(player);
    }

    public void sendHireRequest(Player player, Company company, Player target, Role role) {
        if (plugin.getHireRequest().containsKey(target)) {
            player.sendMessage(text("§c" + target.getName() + " ha già una richiesta di assunzione."));
            return;
        }

        target.sendMessage(text(""));
        target.sendMessage(text("§a" + Utils.toSmallText("richiesta di assunzione")));
        target.sendMessage(text("§7L'azienda §f" + company.getName() + " §7vuole assumerti come §f" + role.getName() + "§7."));
        target.sendMessage(text("§7Clicca ").append(text("§fqui").clickEvent(ClickEvent.runCommand("/azienda accettalavoro")).hoverEvent(HoverEvent.showText(text("§a" + Utils.toSmallText("accetta"))))).append(text(" §7per accettare il contratto di lavoro.")));
        target.sendMessage(text(""));
        player.sendMessage(text("§aContratto di assunzione inviato con successo."));
        plugin.getHireRequest().put(target, role);
    }
}
