package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Role;
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

import java.util.*;

import static net.kyori.adventure.text.Component.text;

public class CompanyCommand implements CommandExecutor, TabCompleter {

    private final Companies plugin;

    public CompanyCommand(Companies plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return List.of();

        if (args.length == 1) {
            return List.of("info", "ruoli");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("ruoli")) {
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

        if (args.length >= 2 && args[0].equalsIgnoreCase("info")) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("§cAzienda non trovata."));
                return true;
            }

            Company company = optionalCompany.get();
            OfflinePlayer director = Bukkit.getOfflinePlayer(UUID.fromString(company.getDirector()));

            if (!plugin.getCompanyUtils().isDirector(player, company)) {
                player.sendMessage(text("§cNon sei il direttore di quest'azienda."));
                return true;
            }

            player.sendMessage(text());
            player.sendMessage(text("§a" + Utils.toSmallText("informazioni azienda ") + "§l" + Utils.toSmallText(company.getName()) + "§a:"));
            player.sendMessage(text("§8▪ §7ID: §f#" + company.getId()));
            player.sendMessage(text("§8▪ §7Direttore: §f" + director.getName()));
            player.sendMessage(text("§8▪ §7Bilancio: §f" + company.getBalance() + "€"));
            player.sendMessage(text("§8▪ §7Data crezione: §f" + Utils.formatDate(company.getWhenCreated())));
            player.sendMessage(text());
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("ruoli")) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("§cAzienda non trovata."));
                return true;
            }

            Company company = optionalCompany.get();

            if (!plugin.getCompanyUtils().isDirector(player, company)) {
                player.sendMessage(text("§cNon sei il direttore di quest'azienda."));
                return true;
            }

            List<Role> roles = plugin.getRoleUtils().getRoles(company);
            if (roles.isEmpty()) {
                player.sendMessage(text("§cNon ci sono ruoli in quest'azienda."));
                return true;
            }

            roles.sort(Comparator.comparing(Role::getType));

            player.sendMessage(text());
            player.sendMessage(text("§a" + Utils.toSmallText("ruoli azienda ") + "§l" + Utils.toSmallText(company.getName()) + "§a:"));
            for (Role role : roles) {
                player.sendMessage(text("§8▪ §7" + role.getName()));
            }
            player.sendMessage(text());
        } else {
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String command) {
        player.sendMessage("");
        player.sendMessage(text("§c§l" + Utils.toSmallText("aiuto comandi ") + "§7(/" + Utils.toSmallText(command) + ")"));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " info <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("guarda le info di un'azienda"))))));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " ruoli <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("guarda i ruoli di un'azienda"))))));
        player.sendMessage(text("§7§o(( " + Utils.toSmallText("trascina il cursore per maggiori informazioni") + " ))"));
        player.sendMessage("");
    }
}
