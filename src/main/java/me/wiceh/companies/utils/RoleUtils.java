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
}
