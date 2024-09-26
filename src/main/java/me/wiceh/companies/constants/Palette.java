package me.wiceh.companies.constants;

public enum Palette {
    BLUE("#0FA2E4"),
    GREEN("#82CA17"),
    RED("#DB2727"),
    YELLOW("#F29D0B");

    private final String hex;

    Palette(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }
}
