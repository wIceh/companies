package me.wiceh.companies.constants;

public enum Icon {
    ERROR_BLUE("\uE40D"),
    ERROR_GREEN("\uE40F"),
    ERROR_RED("\uE40E"),
    ERROR_YELLOW("\uE410");

    private final String icon;

    Icon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
