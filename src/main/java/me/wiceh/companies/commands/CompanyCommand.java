package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.inventories.CompaniesInventory;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Employee;
import me.wiceh.companies.objects.HelpCommand;
import me.wiceh.companies.objects.Role;
import me.wiceh.companies.utils.Utils;
import net.kyori.adventure.text.event.ClickEvent;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
            return List.of("lista", "ruoli", "assumi", "dipendenti", "licenzia");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("ruoli") || args[0].equalsIgnoreCase("assumi") || args[0].equalsIgnoreCase("dipendenti")) {
                return plugin.getCompanyUtils().getCompanies().stream().map(Company::getName).toList();
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("licenzia")) {
                return plugin.getCompanyUtils().getCompanies().stream().map(Company::getName).toList();
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("assumi")) {
                Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(args[1]);
                if (optionalCompany.isEmpty()) return List.of();
                Company company = optionalCompany.get();

                return plugin.getRoleUtils().getRoles(company).stream().map(Role::getName).toList();
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

        if (args.length == 2 && args[0].equalsIgnoreCase("ruoli")) {
            String name = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Azienda non trovata. \n"));
                return true;
            }

            Company company = optionalCompany.get();

            if (!(plugin.getCompanyUtils().isDirector(player, company) || player.hasPermission("azienda.admin"))) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Non sei il direttore di quest'azienda. \n"));
                return true;
            }

            List<Role> roles = plugin.getRoleUtils().getRoles(company);
            if (roles.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Non ci sono ruoli in quest'azienda. \n"));
                return true;
            }

            roles.sort(Comparator.comparing(Role::getType));

            player.sendMessage(text());
            player.sendMessage(text("§a" + Utils.toSmallText("ruoli azienda") + " §l" + company.getName().toUpperCase()));
            player.sendMessage(text());
            for (int i = 0; i < roles.size(); i++) {
                Role role = roles.get(i);
                player.sendMessage(text("§a" + (i + 1) + ". §7" + role.getName() + " §f(" + role.getType().getName() + ")"));
            }
            player.sendMessage(text());
        } else if (args.length == 4 && args[0].equalsIgnoreCase("assumi")) {
            String companyName = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(companyName);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Azienda non trovata. \n"));
                return true;
            }

            Company company = optionalCompany.get();
            if (!(plugin.getCompanyUtils().isDirector(player, company) || player.hasPermission("azienda.admin"))) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Non sei il direttore di quest'azienda. \n"));
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Cittadino non trovato. \n"));
                return true;
            }

            String roleName = args[3];

            Optional<Role> optionalRole = plugin.getRoleUtils().getRole(roleName, company);
            if (optionalRole.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Ruolo non trovato. \n"));
                return true;
            }

            Optional<Employee> optionalEmployee = plugin.getEmployeeUtils().getEmployee(company, target);
            if (optionalEmployee.isPresent()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Questo cittadino lavora già in quest'azienda. \n"));
                return true;
            }

            Role role = optionalRole.get();

            if (plugin.getHireRequest().containsKey(target)) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Questo cittadino ha già una richiesta di assunzione in attesa. \n"));
                return true;
            }

            target.sendMessage(text(""));
            target.sendMessage(text("§a" + Utils.toSmallText("richiesta di assunzione")));
            target.sendMessage(text("§7L'azienda §f" + company.getName() + " §7vuole assumerti come §f" + role.getName() + "§7."));
            target.sendMessage(text("§7Clicca ").append(text("§fqui").clickEvent(ClickEvent.runCommand("/azienda accettalavoro")).hoverEvent(HoverEvent.showText(text("§a" + Utils.toSmallText("accetta"))))).append(text(" §7per accettare il contratto di lavoro.")));
            target.sendMessage(text(""));
            player.sendMessage(text("\n §aᴄᴏɴᴛʀᴀᴛᴛᴏ ɪɴᴠɪᴀᴛᴏ \n §7Contratto di lavoro inviato con successo. \n"));
            plugin.getHireRequest().put(target, role);

            // new HireInventory(plugin).open1(player, company);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("accettalavoro")) {
            if (!plugin.getHireRequest().containsKey(player)) {
                player.sendMessage(text("§cNon hai richieste in sospeso."));
                return true;
            }

            Role role = plugin.getHireRequest().get(player);
            String group = role.getGroup();

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add " + group);

            player.sendMessage(text("\n §aѕᴇɪ ѕᴛᴀᴛᴏ ᴀѕѕᴜɴᴛᴏ \n §7Sei stato assunto come §f" + role.getName() + " §7nell'azienda §f" + role.getCompany().getName() + "§7. \n"));
            plugin.getHireRequest().remove(player);
            plugin.getEmployeeUtils().addEmployee(role.getCompany(), player, role);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("dipendenti")) {
            String name = args[1];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("§cAzienda non trovata."));
                return true;
            }

            Company company = optionalCompany.get();

            if (!(plugin.getCompanyUtils().isDirector(player, company) || player.hasPermission("azienda.admin"))) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Non sei il direttore di quest'azienda. \n"));
                return true;
            }

            List<Employee> employees = plugin.getEmployeeUtils().getEmployees(company);
            if (employees.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Quest'azienda non ha dipendenti. \n"));
                return true;
            }

            player.sendMessage(text());
            player.sendMessage(text("§a" + Utils.toSmallText("dipendenti azienda") + " §l" + company.getName().toUpperCase()));
            player.sendMessage(text());
            for (Employee employee : plugin.getEmployeeUtils().getEmployees(company)) {
                player.sendMessage(text("§a- §7" + employee.getPlayer().getName() + " §f(" + employee.getRole().getName() + ")"));
            }
            player.sendMessage(text());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("licenzia")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (!target.hasPlayedBefore()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Questo cittadino non è mai entrato in città. \n"));
                return true;
            }

            String name = args[2];

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Azienda non trovata. \n"));
                return true;
            }

            Company company = optionalCompany.get();

            if (!(plugin.getCompanyUtils().isDirector(player, company) || player.hasPermission("azienda.admin"))) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Non sei il direttore di quest'azienda. \n"));
                return true;
            }

            List<Employee> employees = plugin.getEmployeeUtils().getEmployees(company);
            boolean isEmployee = employees.stream().anyMatch(employee -> employee.getPlayer().getUniqueId().toString().equals(target.getUniqueId().toString()));
            if (!isEmployee) {
                player.sendMessage(text("\n §cᴇʀʀᴏʀᴇ \n §7Questo cittadino non lavora in quest'azienda. \n"));
                return true;
            }

            Optional<Employee> optionalEmployee = plugin.getEmployeeUtils().getEmployee(company, target);
            if (optionalEmployee.isEmpty()) return true;

            Employee employee = optionalEmployee.get();
            plugin.getEmployeeUtils().removeEmployee(company, target).thenAccept(result -> {
                if (result) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent remove " + employee.getRole().getGroup()));
                    if (target.isOnline())
                        target.getPlayer().sendMessage(text("\n §cѕᴇɪ ѕᴛᴀᴛᴏ ʟɪᴄᴇɴᴢɪᴀᴛᴏ \n §7Sei stato licenziato dall'azienda §f" + company.getName() + "§7. \n"));
                    player.sendMessage(text("\n §aʟɪᴄᴇɴᴢɪᴀᴍᴇɴᴛᴏ \n §7Cittadino licenziato con successo. \n"));
                    return;
                }
                player.sendMessage(text("§cErrore."));
            });
        } else if (args.length == 1 && args[0].equalsIgnoreCase("lista")) {
            new CompaniesInventory(plugin).open(player);
        } else {
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String label) {
        List<me.wiceh.companies.objects.Command> commands = new ArrayList<>();
        commands.add(new me.wiceh.companies.objects.Command("lista", "Visualizza la lista delle aziende"));
        commands.add(new me.wiceh.companies.objects.Command("ruoli <azienda>", "Visualizza i ruoli di un'azienda"));
        commands.add(new me.wiceh.companies.objects.Command("assumi <azienda> <dipendente> <ruolo>", "Assumi un cittadino"));
        commands.add(new me.wiceh.companies.objects.Command("licenzia <player> <azienda>", "Licenzia un cittadino"));
        commands.add(new me.wiceh.companies.objects.Command("dipendenti <azienda>", "Visualizza i dipendenti di un'azienda"));


        HelpCommand helpCommand = new HelpCommand(label, commands);
        helpCommand.send(player);
    }
}
