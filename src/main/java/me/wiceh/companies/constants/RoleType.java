package me.wiceh.companies.constants;

public enum RoleType {
    DIRETTORE,
    VICE_DIRETTORE,
    DIPENDENTE;

    public String getName() {
        switch (this) {
            case DIRETTORE -> {
                return "Direttore";
            }
            case VICE_DIRETTORE -> {
                return "Vice Direttore";
            }
            case DIPENDENTE -> {
                return "Membro";
            }
            default -> {
                return this.name();
            }
        }
    }
}
