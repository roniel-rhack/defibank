package cu.rm.defibank.objects;

public class Transaction {
    protected String transactionId;
    protected String appName;
    protected float importe;

    public Transaction(String transactionId, String appName, float importe) {
        this.transactionId = transactionId;
        this.appName = appName;
        this.importe = importe;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }
}
