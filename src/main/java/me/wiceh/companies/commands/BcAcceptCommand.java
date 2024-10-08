package me.wiceh.companies.commands;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Broadcast;
import me.wiceh.companies.objects.Company;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class BcAcceptCommand implements CommandExecutor {

    private final Companies plugin;

    public BcAcceptCommand(Companies plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!plugin.getBroadcastMap().containsKey(player)) {
            player.sendMessage(text("§cNon hai nessuna richiesta in sospeso."));
            return true;
        }

        Broadcast broadcast = plugin.getBroadcastMap().get(player);
        Company company = broadcast.getCompany();

        plugin.getBroadcastMap().remove(player);

        plugin.getBroadcastUtils().addBroadcastLog(company).thenAccept(result -> {
            if (result) {
                Bukkit.broadcast(text(broadcast.getOpeningMessage()));
                return;
            }
            player.sendMessage(text("§cErrore."));
        });

        return true;
    }
}
