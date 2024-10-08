package me.wiceh.companies.database;

import me.wiceh.companies.Companies;
import org.bukkit.Bukkit;

import java.sql.*;

public class Database {

    private Connection connection;
    private final Companies plugin;

    public Database(Companies plugin) {
        this.plugin = plugin;
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFile().getAbsolutePath());
            plugin.getLogger().info("Connessione al database effettuata con successo.");
            createCompaniesTable();
            createCashRegistersTable();
            createReceiptsTable();
            createBroadcastsLogs();
            createRolesTable();
            createEmployeesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCompaniesTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS companies
                (
                    id                  INTEGER             PRIMARY KEY,
                    name                VARCHAR(50)         NOT NULL,
                    director            VARCHAR(36)         NOT NULL,
                    balance             INTEGER             NOT NULL,
                    whenCreated         TIMESTAMP           NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRolesTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS roles
                (
                    id                  INTEGER             PRIMARY KEY,
                    company             INTEGER             NOT NULL,
                    name                VARCHAR(50)         NOT NULL,
                    "group"             VARCHAR(50)         NOT NULL,
                    type                VARCHAR(50)         NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createEmployeesTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS employees
                (
                    player              VARCHAR(36)         NOT NULL,
                    company             INTEGER             NOT NULL,
                    role                INTEGER             NOT NULL,
                    when_hired          TIMESTAMP           NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCashRegistersTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS cash_registers
                (
                    location    VARCHAR(50)     PRIMARY KEY,
                    company     VARCHAR(50)     NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createReceiptsTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS receipts
                (
                    id              INTEGER             PRIMARY KEY,
                    company         VARCHAR(50)         NOT NULL,
                    customer        VARCHAR(36)         NOT NULL,
                    cashier         VARCHAR(36)         NOT NULL,
                    products        VARCHAR(50)         NOT NULL,
                    price           INTEGER             NOT NULL,
                    date            TIMESTAMP           NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBroadcastsLogs() {
        String query = """
                CREATE TABLE IF NOT EXISTS broadcasts_logs
                (
                    id                  INTEGER             PRIMARY KEY,
                    company             VARCHAR(50)         NOT NULL,
                    lastTimeSent        VARCHAR(36)         NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
