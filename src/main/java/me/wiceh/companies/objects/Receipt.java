package me.wiceh.companies.objects;

import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;

public class Receipt {

    private final int id;
    private Company company;
    private OfflinePlayer customer;
    private OfflinePlayer cashier;
    private String products;
    private int price;
    private Timestamp date;

    public Receipt(int id, Company company, OfflinePlayer customer, OfflinePlayer cashier, String products, int price, Timestamp date) {
        this.id = id;
        this.company = company;
        this.customer = customer;
        this.cashier = cashier;
        this.products = products;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public OfflinePlayer getCustomer() {
        return customer;
    }

    public void setCustomer(OfflinePlayer customer) {
        this.customer = customer;
    }

    public OfflinePlayer getCashier() {
        return cashier;
    }

    public void setCashier(OfflinePlayer cashier) {
        this.cashier = cashier;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp timestamp) {
        this.date = timestamp;
    }
}
