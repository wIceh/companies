package me.wiceh.companies.listeners;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Broadcast;
import me.wiceh.companies.objects.BroadcastLog;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Employee;
import me.wiceh.companies.utils.Utils;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class BcsignInteractListener implements Listener {

    private final Companies plugin;

    public BcsignInteractListener(Companies plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBcsignInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null || !(block.getState() instanceof Sign sign)) return;

            event.setCancelled(true);

            Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(ChatColor.stripColor(sign.getLine(3)));
            if (optionalCompany.isEmpty()) return;

            Company company = optionalCompany.get();
            Optional<Employee> optionalEmployee = plugin.getEmployeeUtils().getEmployee(company, player);
            if (optionalEmployee.isEmpty()) return;

            Optional<BroadcastLog> optionalBroadcastLog = plugin.getBroadcastUtils().getLastBroadcastLog(company);
            if (optionalBroadcastLog.isPresent()) {
                BroadcastLog broadcastLog = optionalBroadcastLog.get();
                long lastTimeSent = broadcastLog.getLastTimeSent();

                long fifteenMinutesInMillis = 15 * 60 * 1000;
                long millisRemaining = fifteenMinutesInMillis - (System.currentTimeMillis() - lastTimeSent);
                long seconds = millisRemaining / 1000;

                if (!Utils.hasFifteenMinutesPassed(lastTimeSent)) {
                    player.sendMessage(text("§cDevi aspettare §7" + Utils.formatTime(seconds) + " §cprima di mandare un altro annuncio."));
                    return;
                }

                // TODO: get broadcast

                plugin.getBroadcastMap().put(player, new Broadcast(company, "a", "a", "a"));
                player.sendMessage(text("\n §a" + Utils.toSmallText("conferma") + " \n §7Clicca ").append(text("§fqui").clickEvent(ClickEvent.runCommand("/bcaccept")).hoverEvent(HoverEvent.showText(text("§a" + Utils.toSmallText("accetta")))).append(text(" §7per inviare l'annuncio. \n"))));
            } else {
                plugin.getBroadcastMap().put(player, new Broadcast(company, "a", "a", "a"));
                player.sendMessage(text("\n §a" + Utils.toSmallText("conferma") + " \n §7Clicca ").append(text("§fqui").clickEvent(ClickEvent.runCommand("/bcaccept")).hoverEvent(HoverEvent.showText(text("§a" + Utils.toSmallText("accetta"))))).append(text(" §7per inviare l'annuncio. \n")));
            }
        }
    }
}
