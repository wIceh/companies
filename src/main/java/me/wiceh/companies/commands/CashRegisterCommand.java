package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.Icon;
import me.wiceh.companies.objects.CashRegister;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CashRegisterCommand implements CommandExecutor, TabCompleter {

    private final Companies plugin;

    public CashRegisterCommand(Companies plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return List.of();

        if (args.length == 1) {
            return List.of("crea");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("crea")) {
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

        if (args.length >= 2 && args[0].equalsIgnoreCase("crea")) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                Utils.sendMessage(player, Icon.ERROR_YELLOW, "Azienda non trovata.");
                return true;
            }

            Company company = optionalCompany.get();

            if (!plugin.getCompanyUtils().isDirector(player, company)) {
                Utils.sendMessage(player, Icon.ERROR_RED, "Non sei il direttore di quest'azienda.");
                return true;
            }

            Entity entity = player.getTargetEntity(5);
            if (!(entity instanceof ArmorStand armorStand)) {
                Utils.sendMessage(player, Icon.ERROR_YELLOW, "Devi guardare una cassa.");
                return true;
            }

            if (!plugin.getCashRegisterUtils().isCashRegister(armorStand)) {
                Utils.sendMessage(player, Icon.ERROR_YELLOW, "Devi guardare una cassa.");
                return true;
            }

            Optional<CashRegister> optionalCashRegister = plugin.getCashRegisterUtils().getCashRegister(armorStand.getLocation());
            if (optionalCashRegister.isPresent()) {
                Utils.sendMessage(player, Icon.ERROR_RED, "Questa cassa è già configurata.");
                return true;
            }

            plugin.getCashRegisterUtils().addCashRegister(company, armorStand.getLocation()).thenAccept(result -> {
                if (result) {
                    Utils.sendMessage(player, Icon.ERROR_GREEN, "Cassa configurata con successo.");
                    return;
                }
                Utils.sendMessage(player, Icon.ERROR_RED, "Si sono riscontrati dei problemi nel configurare questa cassa.");
            });
        } else {
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String command) {
        player.sendMessage("");
        player.sendMessage("§6§l" + command.toUpperCase());
        player.sendMessage(" §8| §e/" + command + " crea <azienda>");
        player.sendMessage("");
    }
}
