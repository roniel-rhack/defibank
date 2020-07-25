package cu.rm.defibank.objects;

import java.io.Serializable;

public class Item implements Serializable {
    protected String title;
    protected String description;
    protected double tip;
    protected double discount;
    protected double cost;

    public Item(String title, String description, double tip, double discount, double cost) {
        this.title = title;
        this.description = description;
        this.tip = tip;
        this.discount = discount;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
