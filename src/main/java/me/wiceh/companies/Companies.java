package me.wiceh.companies;

import me.wiceh.companies.commands.AdminCompanyCommand;
import me.wiceh.companies.commands.CompanyCommand;
import me.wiceh.companies.commands.CashRegisterCommand;
import me.wiceh.companies.database.Database;
import me.wiceh.companies.listeners.CashRegisterBreakListener;
import me.wiceh.companies.listeners.CashRegisterInteractListener;
import me.wiceh.companies.listeners.InventoryInteractListener;
import me.wiceh.companies.objects.Company;
import me.wiceh.companies.utils.RoleUtils;
import me.wiceh.companies.utils.BroadcastUtils;
import me.wiceh.companies.utils.CashRegisterUtils;
import me.wiceh.companies.utils.CompanyUtils;
import me.wiceh.companies.utils.ReceiptUtils;
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
    private RoleUtils roleUtils;

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
        this.roleUtils = new RoleUtils(this);

        getCommand("company").setExecutor(new CompanyCommand(this));
        getCommand("admincompany").setExecutor(new AdminCompanyCommand(this));
        getCommand("cashregister").setExecutor(new CashRegisterCommand(this));
        getServer().getPluginManager().registerEvents(new CashRegisterInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new CashRegisterBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryInteractListener(this), this);
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

    public RoleUtils getRoleUtils() {
        return roleUtils;
    }
}
