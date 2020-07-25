package cu.rm.defibank.objects;

import java.io.Serializable;
import java.util.List;

public class Pay implements Serializable {
    protected String transactionId;
    protected String application;
    protected float discounts;
    protected float shipment;
    protected float taxs;
    protected float tips;
    protected float total;
    protected List<Item> items;

    public Pay(String transactionId, String application, float total, float discounts, float shipment, float taxs, float tips, List<Item> items) {
        this.transactionId = transactionId;
        this.application = application;
        this.total = total;
        this.discounts = discounts;
        this.shipment = shipment;
        this.taxs = taxs;
        this.tips = tips;
        this.items = items;
    }

    public Pay(String transactionId, String application, float total, float discounts, float shipment, float taxs, float tips) {
        this.transactionId = transactionId;
        this.application = application;
        this.total = total;
        this.discounts = discounts;
        this.shipment = shipment;
        this.taxs = taxs;
        this.tips = tips;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getDiscounts() {
        return discounts;
    }

    public void setDiscounts(float discounts) {
        this.discounts = discounts;
    }

    public float getShipment() {
        return shipment;
    }

    public void setShipment(float shipment) {
        this.shipment = shipment;
    }

    public float getTaxs() {
        return taxs;
    }

    public void setTaxs(float taxs) {
        this.taxs = taxs;
    }

    public float getTips() {
        return tips;
    }

    public void setTips(float tips) {
        this.tips = tips;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
