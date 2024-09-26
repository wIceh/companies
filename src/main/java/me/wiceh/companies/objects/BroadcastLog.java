package me.wiceh.companies.objects;

public class BroadcastLog {

    private final int id;
    private Company company;
    private long lastTimeSent;

    public BroadcastLog(int id, Company company, long lastTimeSent) {
        this.id = id;
        this.company = company;
        this.lastTimeSent = lastTimeSent;
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

    public long getLastTimeSent() {
        return lastTimeSent;
    }

    public void setLastTimeSent(long lastTimeSent) {
        this.lastTimeSent = lastTimeSent;
    }
}
