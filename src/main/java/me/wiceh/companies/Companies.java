package me.wiceh.companies;

import me.wiceh.companies.commands.*;
import me.wiceh.companies.database.Database;
import me.wiceh.companies.listeners.BcsignInteractListener;
import me.wiceh.companies.listeners.CashRegisterBreakListener;
import me.wiceh.companies.listeners.CashRegisterInteractListener;
import me.wiceh.companies.listeners.InventoryInteractListener;
import me.wiceh.companies.objects.Broadcast;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.objects.Role;
import me.wiceh.companies.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Companies extends JavaPlugin {

    private File dataFile;
    private Database database;
    private CompanyUtils companyUtils;
    private CashRegisterUtils cashRegisterUtils;
    private ReceiptUtils receiptUtils;
    private BroadcastUtils broadcastUtils;
    private Map<Player, Company> companyMap;
    private Map<Player, Role> hireRequest;
    private Map<Player, Broadcast> broadcastMap;
    private RoleUtils roleUtils;
    private EmployeeUtils employeeUtils;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        loadDataFile();
        saveDefaultConfig();

        this.database = new Database(this);
        this.companyUtils = new CompanyUtils(this);
        this.cashRegisterUtils = new CashRegisterUtils(this);
        this.receiptUtils = new ReceiptUtils(this);
        this.broadcastUtils = new BroadcastUtils(this);
        this.companyMap = new HashMap<>();
        this.hireRequest = new HashMap<>();
        this.roleUtils = new RoleUtils(this);
        this.employeeUtils = new EmployeeUtils(this);
        this.broadcastMap = new HashMap<>();

        getCommand("company").setExecutor(new CompanyCommand(this));
        getCommand("admincompany").setExecutor(new AdminCompanyCommand(this));
        getCommand("cashregister").setExecutor(new CashRegisterCommand(this));
        getCommand("bcsign").setExecutor(new BcsignCommand(this));
        getCommand("bcaccept").setExecutor(new BcAcceptCommand(this));
        getServer().getPluginManager().registerEvents(new CashRegisterInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new CashRegisterBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new BcsignInteractListener(this), this);
    }

    @Override
    public void onDisable() {
        database.disconnect();
    }

    private void loadDataFile() {
        dataFile = new File(getDataFolder().getAbsolutePath() + "/data.db");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getDataFile() {
        return dataFile;
    }

    public Database getDatabase() {
        return database;
    }

    public CompanyUtils getCompanyUtils() {
        return companyUtils;
    }

    public CashRegisterUtils getCashRegisterUtils() {
        return cashRegisterUtils;
    }

    public ReceiptUtils getReceiptUtils() {
        return receiptUtils;
    }

    public BroadcastUtils getBroadcastUtils() {
        return broadcastUtils;
    }

    public Map<Player, Company> getCompanyMap() {
        return companyMap;
    }

    public Map<Player, Role> getHireRequest() {
        return hireRequest;
    }

    public RoleUtils getRoleUtils() {
        return roleUtils;
    }

    public EmployeeUtils getEmployeeUtils() {
        return employeeUtils;
    }

    public Map<Player, Broadcast> getBroadcastMap() {
        return broadcastMap;
    }
}
