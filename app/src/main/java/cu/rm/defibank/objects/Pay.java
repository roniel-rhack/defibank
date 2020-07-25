package cu.rm.defibank.objects;

import java.io.Serializable;
import java.util.List;

public class Pay implements Serializable {
    protected String transactionId;
    protected String application;
    protected double discounts;
    protected double shipment;
    protected double taxs;
    protected double tips;
    protected double total;
    protected List<Item> items;

    public Pay(String transactionId, String application, double total, double discounts, double shipment, double taxs, double tips, List<Item> items) {
        this.transactionId = transactionId;
        this.application = application;
        this.total = total;
        this.discounts = discounts;
        this.shipment = shipment;
        this.taxs = taxs;
        this.tips = tips;
        this.items = items;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(double discounts) {
        this.discounts = discounts;
    }

    public double getShipment() {
        return shipment;
    }

    public void setShipment(double shipment) {
        this.shipment = shipment;
    }

    public double getTaxs() {
        return taxs;
    }

    public void setTaxs(double taxs) {
        this.taxs = taxs;
    }

    public double getTips() {
        return tips;
    }

    public void setTips(double tips) {
        this.tips = tips;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
