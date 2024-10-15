package com.pluralsight;

public class Transactions {
private String date;
private String time;
private String description;
private String vendor;
private double price;

    public Transactions(String date, String time, String description, String vendor, double price) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return price;
    }

    public void getAmount(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", vendor='" + vendor + '\'' +
                ", price=" + price +
                '}';
    }
}

