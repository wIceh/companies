package me.wiceh.companies.objects;

import org.bukkit.Location;

public class CashRegister {

    private Location location;
    private Company company;

    public CashRegister(Location location, Company company) {
        this.location = location;
        this.company = company;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
