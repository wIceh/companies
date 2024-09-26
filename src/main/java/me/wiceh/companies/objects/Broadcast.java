package me.wiceh.companies.objects;

public class Broadcast {

    private Company company;
    private String openingMessage;
    private String closingMessage;
    private String temporaryClosureMessage;

    public Broadcast(Company company, String openingMessage, String closingMessage, String temporaryClosureMessage) {
        this.company = company;
        this.openingMessage = openingMessage;
        this.closingMessage = closingMessage;
        this.temporaryClosureMessage = temporaryClosureMessage;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getOpeningMessage() {
        return openingMessage;
    }

    public void setOpeningMessage(String openingMessage) {
        this.openingMessage = openingMessage;
    }

    public String getClosingMessage() {
        return closingMessage;
    }

    public void setClosingMessage(String closingMessage) {
        this.closingMessage = closingMessage;
    }

    public String getTemporaryClosureMessage() {
        return temporaryClosureMessage;
    }

    public void setTemporaryClosureMessage(String temporaryClosureMessage) {
        this.temporaryClosureMessage = temporaryClosureMessage;
    }
}
