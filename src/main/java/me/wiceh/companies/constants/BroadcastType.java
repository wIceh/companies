package me.wiceh.companies.constants;

public enum BroadcastType {
    OPENING,
    CLOSING,
    TEMPORARY_CLOSURE;

    public String lowerCase() {
        return name().toLowerCase();
    }
}
