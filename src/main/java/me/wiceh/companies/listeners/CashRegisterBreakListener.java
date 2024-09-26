package me.wiceh.companies.listeners;

import me.wiceh.companies.Companies;
import me.wiceh.companies.objects.CashRegister;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class CashRegisterBreakListener implements Listener {

    private final Companies plugin;

    public CashRegisterBreakListener(Companies plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCashRegisterBreak(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ArmorStand armorStand)) return;

        Optional<CashRegister> optionalCashRegister = plugin.getCashRegisterUtils().getCashRegister(armorStand.getLocation());
        if (optionalCashRegister.isEmpty()) return;

        plugin.getCashRegisterUtils().deleteCashRegister(armorStand.getLocation());
    }
}
