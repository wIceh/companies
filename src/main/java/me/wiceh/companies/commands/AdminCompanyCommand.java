package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.inventories.AddRoleInventory;
import me.wiceh.companies.inventories.DelRoleInventory;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.HelpCommand;
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
            return List.of("crea", "elimina", "addRuolo", "delRuolo");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("elimina") || args[0].equalsIgnoreCase("addruolo") || args[0].equalsIgnoreCase("delruolo")) {
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

        if (args.length == 3 && args[0].equalsIgnoreCase("crea")) {
            String name = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isPresent()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Esiste già un'azienda con questo nome. \n"));
                return true;
            }

            OfflinePlayer director = Bukkit.getOfflinePlayer(args[2]);
            if (!director.hasPlayedBefore()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Questo cittadino non è mai entrato in città. \n"));
                return true;
            }

            plugin.getCompanyUtils().addCompany(name, director).thenAccept(result -> {
                if (result.isPresent()) {
                    Company company = result.get();
                    player.sendMessage(text("\n §aᴀᴢɪᴇɴᴅᴀ ᴄʀᴇᴀᴛᴀ \n §7Hai creato l'azienda §f" + name + " §7con successo. §f(#" + company.getId() + ") \n"));
                    return;
                }
                player.sendMessage(text("§cᴇʀʀᴏʀᴇ."));
            });
        } else if (args.length == 2 && args[0].equalsIgnoreCase("elimina")) {
            String name = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Azienda non trovata. \n"));
                return true;
            }

            plugin.getCompanyUtils().deleteCompany(name).thenAccept(result -> {
                if (result) {
                    player.sendMessage(text("\n §cᴀᴢɪᴇɴᴅᴀ ᴇʟɪᴍɪɴᴀᴛᴀ \n §7L'azienda §f" + optionalCompany.get().getName() + " §7è stata eliminata con successo. \n"));
                    return;
                }
                player.sendMessage(text("§cᴇʀʀᴏʀᴇ."));
            });
        } else if (args.length == 2 && args[0].equalsIgnoreCase("addruolo")) {
            String name = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Azienda non trovata. \n"));
                return true;
            }

            Company company = optionalCompany.get();
            new AddRoleInventory(plugin).open1(player, company);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delruolo")) {
            String name = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Azienda non trovata. \n"));
                return true;
            }

            Company company = optionalCompany.get();
            new DelRoleInventory(plugin).open(player, company);
        } else {
            sendHelp(player, label);
        }

        return true;
    }

    /*
    private void sendHelp(Player player, String command) {
        player.sendMessage("");
        player.sendMessage(text("§c§l" + Utils.toSmallText("aiuto comandi ") + "§7(/" + Utils.toSmallText(command) + ")"));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " crea <nome> <direttore>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("crea un'azienda"))))));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " elimina <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("elimina un'azienda"))))));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " addRuolo <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("aggiungi un ruolo ad un'azienda"))))));
        player.sendMessage(text(" §8▪ ").append(text("§e/" + command + " delRuolo <azienda>").hoverEvent(HoverEvent.showText(text("§7" + Utils.toSmallText("elimina un ruolo di un'azienda"))))));
        player.sendMessage(text("§7§o(( " + Utils.toSmallText("trascina il cursore per maggiori informazioni") + " ))"));
        player.sendMessage("");
    }
     */

    private void sendHelp(Player player, String label) {
        List<me.wiceh.companies.objects.Command> commands = new ArrayList<>();
        commands.add(new me.wiceh.companies.objects.Command("crea <nome> <direttore>", "Crea un'azienda"));
        commands.add(new me.wiceh.companies.objects.Command("elimina <azienda>", "Elimina un'azienda"));
        commands.add(new me.wiceh.companies.objects.Command("addRuolo <azienda>", "Crea un ruolo"));
        commands.add(new me.wiceh.companies.objects.Command("delRuolo <azienda>", "Elimina un ruolo"));

        HelpCommand helpCommand = new HelpCommand(label, commands);
        helpCommand.send(player);
    }
}
