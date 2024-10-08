package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class BcsignCommand implements CommandExecutor {

    private final Companies plugin;

    public BcsignCommand(Companies plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("bcsign.admin")) {
            player.sendMessage(text("§cNon hai il permesso."));
            return true;
        }

        if (args.length >= 1) {
            List<String> nameList = new ArrayList<>(Arrays.asList(args));
            String name = String.join(" ", nameList);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(name);
            if (optionalCompany.isEmpty()) {
                player.sendMessage(text("§cAzienda non trovata."));
                return true;
            }

            Company company = optionalCompany.get();

            Block block = player.getTargetBlockExact(5);
            if (block == null || !(block.getState() instanceof Sign sign)) {
                player.sendMessage(text("§cDevi guardare un cartello."));
                return true;
            }

            sign.setLine(0, "§d[Annuncio]");
            sign.setLine(1, "§0Clicca qui");
            sign.setLine(2, "§0per inviare");
            sign.setLine(3, "§l" + company.getName().toUpperCase());
            sign.update();

            player.sendMessage(text("§aCartello impostato con successo."));
        } else {
            player.sendMessage(text("§cUtilizzo: /" + label + " <azienda>"));
        }

        return true;
    }
}
