package me.wiceh.companies.objects;

import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;

public class Employee {

    private OfflinePlayer player;
    private Company company;
    private Role role;
    private final Timestamp whenHired;

    public Employee(OfflinePlayer player, Company company, Role role, Timestamp whenHired) {
        this.player = player;
        this.company = company;
        this.role = role;
        this.whenHired = whenHired;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Timestamp getWhenHired() {
        return whenHired;
    }
}
