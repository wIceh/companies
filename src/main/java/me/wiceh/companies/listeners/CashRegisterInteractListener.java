package me.wiceh.companies.listeners;

import me.wiceh.companies.Companies;
import me.wiceh.companies.inventories.CashRegisterInventory;
import me.wiceh.companies.objects.CashRegister;
import me.wiceh.companies.objects.Company;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Optional;

public class CashRegisterInteractListener implements Listener {

    private final Companies plugin;

    public CashRegisterInteractListener(Companies plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCashRegisterInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof ArmorStand armorStand)) return;

        Optional<CashRegister> optionalCashRegister = plugin.getCashRegisterUtils().getCashRegister(armorStand.getLocation());
        if (optionalCashRegister.isEmpty()) return;

        CashRegister cashRegister = optionalCashRegister.get();
        Company company = cashRegister.getCompany();

        //String formattedCompany = plugin.getCompanyUtils().getCompany(company.getName());
        //if (!player.hasPermission("cassa.use." + formattedCompany)) return;
        // todo: check if player is an employee

        new CashRegisterInventory(plugin).open(player, company);
    }
}
