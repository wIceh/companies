package me.wiceh.companies.utils;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.BroadcastType;
import me.wiceh.companies.constants.Icon;
import me.wiceh.companies.objects.Broadcast;
import me.wiceh.companies.objects.BroadcastLog;
import me.wiceh.companies.objects.Company;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public class BroadcastUtils {

    private final Companies plugin;

    public BroadcastUtils(Companies plugin) {
        this.plugin = plugin;
    }

    public Optional<Broadcast> getBroadcast(Company company) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("broadcasts." + plugin.getCompanyUtils().getCompany(company.getName()));
        if (section == null) return Optional.empty();

        String openingMessage = section.getString("opening");
        String closingMessage = section.getString("closing");
        String temporaryClosure = section.getString("temporary-closure");

        return Optional.of(new Broadcast(
                company,
                openingMessage,
                closingMessage,
                temporaryClosure
        ));
    }

    public CompletableFuture<Boolean> addBroadcastLog(Company company) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    INSERT INTO broadcasts_logs(id, company, lastTimeSent)
                    VALUES (NULL, ?, ?)
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setString(1, company.getName());
                statement.setLong(2, System.currentTimeMillis());

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public Optional<BroadcastLog> getLastBroadcastLog(Company company) {
        String query = """
                SELECT * FROM broadcasts_logs
                WHERE company = ?
                ORDER BY id DESC
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setString(1, company.getName());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(new BroadcastLog(
                        result.getInt("id"),
                        company,
                        result.getLong("lastTimeSent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void sendBroadcast(Player player, Company company, BroadcastType broadcastType) {
        Optional<Broadcast> optionalBroadcast = plugin.getBroadcastUtils().getBroadcast(company);
        if (optionalBroadcast.isEmpty()) return;

        Broadcast broadcast = optionalBroadcast.get();
        String message = getMessage(broadcast, broadcastType);

        Optional<BroadcastLog> optionalBroadcastLog = plugin.getBroadcastUtils().getLastBroadcastLog(company);
        if (optionalBroadcastLog.isPresent()) {
            BroadcastLog broadcastLog = optionalBroadcastLog.get();
            long lastTimeSent = broadcastLog.getLastTimeSent();

            long fifteenMinutesInMillis = 15 * 60 * 1000;
            long millisRemaining = fifteenMinutesInMillis - (System.currentTimeMillis() - lastTimeSent);
            long seconds = millisRemaining / 1000;

            if (!areFifteenMinutesPassed(lastTimeSent)) {
                Utils.sendMessage(player, Icon.ERROR_YELLOW, "Devi aspettare " + Utils.formatTime(seconds) + " prima di mandare un altro broadcast.");
                player.closeInventory();
                return;
            }

            plugin.getBroadcastUtils().addBroadcastLog(company).thenAccept(result -> {
                if (result) {
                    Bukkit.broadcast(text(Utils.format(message)));
                    return;
                }
                Utils.sendMessage(player, Icon.ERROR_RED, "Errore.");
            });
        } else {
            plugin.getBroadcastUtils().addBroadcastLog(company).thenAccept(result -> {
                if (result) {
                    Bukkit.broadcast(text(Utils.format(message)));
                    return;
                }
                Utils.sendMessage(player, Icon.ERROR_RED, "Errore.");
            });
        }
    }

    public String getMessage(Broadcast broadcast, BroadcastType broadcastType) {
        String message = "";

        switch (broadcastType) {
            case OPENING -> message = broadcast.getOpeningMessage();
            case CLOSING -> message = broadcast.getClosingMessage();
            case TEMPORARY_CLOSURE -> message = broadcast.getTemporaryClosureMessage();
        }

        return message;
    }

    private boolean areFifteenMinutesPassed(long startTime) {
        long currentTime = System.currentTimeMillis();

        long differenceInMillis = currentTime - startTime;
        long fifteenMinutesInMillis = 15 * 60 * 1000;

        return differenceInMillis >= fifteenMinutesInMillis;
    }
}
