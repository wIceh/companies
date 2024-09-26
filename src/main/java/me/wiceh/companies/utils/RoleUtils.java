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

    public CompletableFuture<Optional<Role>> addRole(Company company, String name, String group, RoleType type) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    INSERT INTO roles(company, name, "group", type)
                    VALUES (?, ?, ?, ?)
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setInt(1, company.getId());
                statement.setString(2, name);
                statement.setString(3, group);
                statement.setString(4, type.name());

                if (statement.executeUpdate() > 0) {
                    return Optional.of(new Role(
                            name,
                            group,
                            company,
                            type
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return Optional.empty();
        });
    }
}
