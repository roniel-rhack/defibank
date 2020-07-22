package cu.rm.defibank.objects;

import java.io.Serializable;

public class Item implements Serializable {
    protected String title;
    protected String description;
    protected String tip;
    protected String discount;
    protected String cost;

    public Item(String title, String description, String tip, String discount, String cost) {
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
