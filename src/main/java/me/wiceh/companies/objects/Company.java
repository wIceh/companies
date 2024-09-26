package me.wiceh.companies.objects;

import java.sql.Timestamp;

public class Company {

    private final int id;
    private String name;
    private String director;
    private int balance;
    private Timestamp whenCreated;

    public Company(int id, String name, String director, int balance, Timestamp whenCreated) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.balance = balance;
        this.whenCreated = whenCreated;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Timestamp whenCreated) {
        this.whenCreated = whenCreated;
    }
}
