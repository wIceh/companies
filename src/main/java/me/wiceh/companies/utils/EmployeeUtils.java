package me.wiceh.companies.utils;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Employee;
import me.wiceh.companies.objects.Role;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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

public class EmployeeUtils {

    private final Companies plugin;

    public EmployeeUtils(Companies plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Boolean> addEmployee(Company company, Player player, Role role) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    INSERT INTO employees(player, company, role, when_hired)
                    VALUES (?, ?, ?, ?)
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setInt(2, company.getId());
                statement.setInt(3, role.getId());
                statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public List<Employee> getEmployees(Company company) {
        String query = """
                SELECT * FROM employees
                WHERE company = ?
                """;
        List<Employee> employees = new ArrayList<>();

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, company.getId());

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Optional<Role> optionalRole = plugin.getRoleUtils().getRole(result.getInt("role"));
                if (optionalRole.isEmpty()) return employees;
                employees.add(new Employee(
                        Bukkit.getOfflinePlayer(UUID.fromString(result.getString("player"))),
                        company,
                        optionalRole.get(),
                        result.getTimestamp("when_hired")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public Optional<Employee> getEmployee(Company company, OfflinePlayer player) {
        String query = """
                SELECT * FROM employees
                WHERE company = ? AND player = ?
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, company.getId());
            statement.setString(2, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Optional<Role> optionalRole = plugin.getRoleUtils().getRole(result.getInt("role"));
                if (optionalRole.isEmpty()) return Optional.empty();
                return Optional.of(new Employee(
                        player,
                        company,
                        optionalRole.get(),
                        result.getTimestamp("when_hired")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public CompletableFuture<Boolean> removeEmployee(Company company, OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                DELETE FROM employees
                WHERE company = ? AND player = ?
                """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setInt(1, company.getId());
                statement.setString(2, player.getUniqueId().toString());

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }
}
