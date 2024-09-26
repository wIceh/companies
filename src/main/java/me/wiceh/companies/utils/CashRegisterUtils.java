package me.wiceh.companies.utils;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.CashRegister;
import me.wiceh.companies.objects.Company;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CashRegisterUtils {

    private final Companies plugin;

    public CashRegisterUtils(Companies plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Boolean> addCashRegister(Company company, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    INSERT INTO cash_registers(location, company)
                    VALUES (?, ?)
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setString(1, LocationUtils.locationToString(location));
                statement.setString(2, company.getName());

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> deleteCashRegister(Location location) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
                    DELETE FROM cash_registers
                    WHERE location = ?
                    """;

            try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
                statement.setString(1, LocationUtils.locationToString(location));

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public Optional<CashRegister> getCashRegister(Location location) {
        String query = """
                SELECT * FROM cash_registers
                WHERE location = ?
                """;

        try (PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement(query)) {
            statement.setString(1, LocationUtils.locationToString(location));

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Optional<Company> optionalCompany = plugin.getCompanyUtils().getCompany(result.getString("company"));
                return optionalCompany.map(company -> new CashRegister(
                        location,
                        company
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean isCashRegister(ArmorStand armorStand) {
        Material item = Material.valueOf(plugin.getConfig().getString("cash_register.material"));
        int customModelData = plugin.getConfig().getInt("cash_register.custom-model-data");

        ItemStack helmet = armorStand.getEquipment().getHelmet();
        if (helmet == null) return false;
        if (helmet.getType() != item) return false;
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasCustomModelData()) return false;

        return meta.getCustomModelData() == customModelData;
    }
}
