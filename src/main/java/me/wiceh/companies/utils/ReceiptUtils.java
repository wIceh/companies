package me.wiceh.companies.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.Icon;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Receipt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public class ReceiptUtils {

    private final Companies plugin;

    public ReceiptUtils(Companies plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Boolean> addReceipt(Company company, Player customer, Player cashier, String products, int price, Timestamp date) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    INSERT INTO receipts(id, company, customer, cashier, products, price, date)
                    VALUES (NULL, ?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setString(1, company.getName());
                statement.setString(2, customer.getUniqueId().toString());
                statement.setString(3, cashier.getUniqueId().toString());
                statement.setString(4, products);
                statement.setInt(5, price);
                statement.setTimestamp(6, date);

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> removeReceipt(int id) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    DELETE FROM receipts
                    WHERE id = ?
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setInt(1, id);

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public List<Receipt> getReceipts(Company company) {
        String query = """
                SELECT * FROM receipts
                WHERE company = ?
                ORDER BY date DESC
                """;
        List<Receipt> receipts = new ArrayList<>();

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setString(1, company.getName());

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                receipts.add(new Receipt(
                        result.getInt("id"),
                        company,
                        Bukkit.getOfflinePlayer(UUID.fromString(result.getString("customer"))),
                        Bukkit.getOfflinePlayer(UUID.fromString(result.getString("cashier"))),
                        result.getString("products"),
                        result.getInt("price"),
                        result.getTimestamp("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receipts;
    }

    public Optional<Receipt> getReceipt(int id) {
        String query = """
                SELECT * FROM receipts
                WHERE id = ?
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Optional<Company> company = plugin.getCompanyUtils().getCompany(result.getString("company"));
                if (company.isEmpty()) return Optional.empty();
                return Optional.of(new Receipt(
                        id,
                        company.get(),
                        Bukkit.getOfflinePlayer(UUID.fromString(result.getString("customer"))),
                        Bukkit.getOfflinePlayer(UUID.fromString(result.getString("cashier"))),
                        result.getString("products"),
                        result.getInt("price"),
                        result.getTimestamp("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void makeReceipt(Player player, Company company, Player target, String products, int price) {
        LocalDateTime dateTime = LocalDateTime.now();
        ItemStack scontrino = ItemBuilder.from(Material.STICK)
                .name(text("§7Scontrino Fiscale"))
                .lore(text("§f"), text("§7Importo §8» §f" + price + "€"), text("§7Azienda §8» §f" + company.getName()), text("§7Acquirente §8» §f" + target.getName()), text("§7Data §8» §f" + Utils.formatDate(dateTime)), text("§7Prodotti §8» §f" + products))
                .model(133)
                .build();

        plugin.getReceiptUtils().addReceipt(company, target, player, products, price, Timestamp.valueOf(dateTime)).thenAccept(result -> {
            if (result) {
                target.getInventory().addItem(scontrino);
                player.sendMessage(text("§aScontrino stampato con successo."));
                plugin.getCompanyUtils().giveMoney(company, price);
                // todo: take money from cashier (player)
                return;
            }
            Utils.sendMessage(player, Icon.ERROR_RED, "Errore.");
        });
    }

    public ItemStack getReceiptItem(Receipt receipt, boolean copy) {
        String name = copy ? "§7Scontrino Fiscale §f(Copia)" : "§7Scontrino Fiscale";

        return ItemBuilder.from(Material.STICK)
                .name(text(name))
                .lore(text("§f"), text("§7Importo §8» §f" + receipt.getPrice() + "€"), text("§7Azienda §8» §f" + receipt.getCompany().getName()), text("§7Acquirente §8» §f" + receipt.getCustomer().getName()), text("§7Data §8» §f" + Utils.formatDate(receipt.getDate())), text("§7Prodotti §8» §f" + receipt.getProducts()))
                .model(133)
                .build();
    }
}
