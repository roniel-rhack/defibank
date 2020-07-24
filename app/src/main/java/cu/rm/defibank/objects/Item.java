package cu.rm.defibank.objects;

import java.io.Serializable;

public class Item implements Serializable {
    protected String title;
    protected String description;
    protected float tip;
    protected float discount;
    protected float cost;

    public Item(String title, String description, float tip, float discount, float cost) {
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

    public float getTip() {
        return tip;
    }

    public void setTip(float tip) {
        this.tip = tip;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
