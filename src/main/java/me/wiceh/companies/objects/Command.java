package me.wiceh.companies.objects;

public class Command {

    private final String usage;
    private final String description;
    private final String permission;

    public Command(String usage, String description, String permission) {
        this.usage = usage;
        this.description = description;
        this.permission = permission;
    }

    public Command(String usage, String description) {
        this.usage = usage;
        this.description = description;
        this.permission = null;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }
}
