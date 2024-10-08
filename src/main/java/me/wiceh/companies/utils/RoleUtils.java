package me.wiceh.companies.utils;

import me.wiceh.companies.Companies;
import me.wiceh.companies.constants.RoleType;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RoleUtils {

    private final Companies plugin;

    public RoleUtils(Companies plugin) {
        this.plugin = plugin;
    }

    public List<Role> getRoles(Company company) {
        String query = """
                SELECT * FROM roles
                WHERE company = ?
                """;
        List<Role> roles = new ArrayList<>();

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, company.getId());

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                roles.add(new Role(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("group"),
                        company,
                        RoleType.valueOf(result.getString("type"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public Optional<Role> getRole(String name, Company company) {
        String query = """
                SELECT * FROM roles
                WHERE company = ? AND name = ? COLLATE NOCASE
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, company.getId());
            statement.setString(2, name);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(result.getInt("company"));
                if (optionalCompany.isEmpty()) return Optional.empty();
                return Optional.of(new Role(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("group"),
                        optionalCompany.get(),
                        RoleType.valueOf(result.getString("type"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<Role> getRole(int id) {
        String query = """
                SELECT * FROM roles
                WHERE id = ?
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(result.getInt("company"));
                if (optionalCompany.isEmpty()) return Optional.empty();
                return Optional.of(new Role(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("group"),
                        optionalCompany.get(),
                        RoleType.valueOf(result.getString("type"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public CompletableFuture<Boolean> addRole(Company company, String role, String group, RoleType type) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                INSERT INTO roles(id, company, name, "group", type)
                VALUES (NULL, ?, ?, ?, ?)
                """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setInt(1, company.getId());
                statement.setString(2, role);
                statement.setString(3, group);
                statement.setString(4, type.name());

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> removeRole(Role role) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                DELETE FROM roles
                WHERE id = ?
                """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setInt(1, role.getId());

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }
}
