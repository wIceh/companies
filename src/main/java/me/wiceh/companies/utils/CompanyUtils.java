package me.wiceh.companies.utils;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CompanyUtils {

    private final Companies plugin;

    public CompanyUtils(Companies plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Optional<Company>> addCompany(String name, OfflinePlayer director) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                INSERT INTO companies(id, name, director, balance, whenCreated)
                VALUES (NULL, ?, ?, ?, ?)
                """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                statement.setString(1, name);
                statement.setString(2, director.getUniqueId().toString());
                statement.setInt(3, 0);
                statement.setTimestamp(4, timestamp);

                if (statement.executeUpdate() > 0) {
                    ResultSet keys = statement.getGeneratedKeys();
                    return Optional.of(new Company(
                            keys.getInt(1),
                            name,
                            director.getUniqueId().toString(),
                            0,
                            timestamp
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return Optional.empty();
        });
    }

    public CompletableFuture<Boolean> deleteCompany(String name) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                DELETE FROM companies
                WHERE name = ? COLLATE NOCASE
                """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setString(1, name);

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> updateBalance(Company company, int value) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                UPDATE companies
                SET balance = ?
                WHERE id = ?
                """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setInt(1, value);
                statement.setInt(2, company.getId());

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public void giveMoney(Company company, int amount) {
        updateBalance(company, company.getBalance() + amount);
    }

    public void removeMoney(Company company, int amount) {
        updateBalance(company, company.getBalance() - amount);
    }

    public Optional<Company> getCompany(int id) {
        String query = """
                SELECT * FROM companies
                WHERE id = ?
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(new Company(
                        id,
                        result.getString("name"),
                        result.getString("director"),
                        result.getInt("balance"),
                        result.getTimestamp("whenCreated")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<Company> getCompany(String name) {
        String query = """
                SELECT * FROM companies
                WHERE name = ? COLLATE NOCASE
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(new Company(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("director"),
                        result.getInt("balance"),
                        result.getTimestamp("whenCreated")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Company> getCompanies() {
        String query = """
                SELECT * FROM companies
                ORDER BY id DESC
                """;
        List<Company> companies = new ArrayList<>();

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                companies.add(new Company(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("director"),
                        result.getInt("balance"),
                        result.getTimestamp("whenCreated")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    public boolean isDirector(OfflinePlayer player, Company company) {
        return player.getUniqueId().toString().equals(company.getDirector());
    }
}
