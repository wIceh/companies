package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.inventories.AddRoleInventory;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class AdminCompanyCommand implements CommandExecutor, TabCompleter {

    private final Companies plugin;

    public AdminCompanyCommand(Companies plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return List.of();

        if (args.length == 1) {
            return List.of("crea", "elimina", "addRuolo");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("elimina")) {
                return plugin.getCompanyUtils().getCompanies().stream().map(Company::getName).toList();
            }
        }

        return List.of();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.getLogger().info("Errore: Questo comando è eseguibile solamente dai giocatori.");
            return true;
        }

        if (!player.hasPermission("azienda.admin")) {
            player.sendMessage(text("§cNon hai il permesso per eseguire questo comando."));
            return true;
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("crea")) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args).subList(1, (args.length - 1)));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isPresent()) {
                player.sendMessage(text("§cEsiste già un'azienda con questo nome."));
                return true;
            }

            OfflinePlayer director = Bukkit.getOfflinePlayer(args[args.length - 1]);
            if (!director.hasPlayedBefore()) {
                player.sendMessage(text("§cQuesto player non è mai entrato in città."));
                return true;
            }

            plugin.getCompanyUtils().addCompany(name, director).thenAccept(result -> {
                if (result.isPresent()) {
                    Company company = result.get();
                    player.sendMessage(text("§aHai creato l'azienda " + name + " con successo. (#" + company.getId() + ")"));
                    return;
                }
                player.sendMessage(text("§cSi sono riscontrati dei problemi nella creazione di questa azienda."));
            });
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("elimina")) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("§cNon esiste nessun'azienda con questo nome."));
                return true;
            }

            plugin.getCompanyUtils().deleteCompany(name).thenAccept(result -> {
                if (result) {
                    player.sendMessage(text("§aAzienda eliminata con successo."));
                    return;
                }
                player.sendMessage(text("§cSi sono riscontrati dei problemi nella eliminazione di questa azienda."));
            });
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("addruolo")) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("§cNon esiste nessun'azienda con questo nome."));
                return true;
            }

            Company company = optionalCompany.get();
            new AddRoleInventory(plugin).open1(player, company);
        } else {
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String command) {
        player.sendMessage("");
        player.sendMessage(text("§c§l" + Utils.toSmallText("aiuto comandi ") + "§7(/" + Utils.toSmallText(command) + ")"));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " crea <nome> <direttore>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("crea un'azienda"))))));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " elimina <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("elimina un'azienda"))))));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " addRuolo <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("aggiungi un ruolo ad un'azienda"))))));
        player.sendMessage(text("§7§o(( " + Utils.toSmallText("trascina il cursore per maggiori informazioni") + " ))"));
        player.sendMessage("");
    }
}
