package me.wiceh.companies.objects;

import java.sql.Timestamp;

public class Company {

    private final int id;
    private String name;
    private Timestamp whenCreated;

    public Company(int id, String name, Timestamp whenCreated) {
        this.id = id;
        this.name = name;
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

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Timestamp whenCreated) {
        this.whenCreated = whenCreated;
    }
}
