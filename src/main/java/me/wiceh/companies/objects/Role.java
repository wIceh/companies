package me.wiceh.companies.objects;

import me.wiceh.companies.constants.RoleType;

public class Role {

    private Company company;
    private String name;
    private String group;
    private RoleType type;

    public Role(String name, String group, Company company, RoleType type) {
        this.name = name;
        this.group = group;
        this.company = company;
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}