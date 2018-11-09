package asch.io.wallet.model.entity;

/**
 * Created by kimziv on 2017/11/1.
 */

public class UIATransferAsset extends BaseAsset {

    /*
     "transactionId": "4c7f362a7e91771b1a3b71acddf5801344cf0ce7861689d152bf38af783c907f",
     "currency": "APP.APT",
     "amount": "399900000000",
     "amountShow": "3999",
     "precision": 8
     */
    public static class UIATransfer {
        private String transactionId;
        private String currency;
        private String amount;
        private String amountShow;
        private int  precision;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmountShow() {
            return amountShow;
        }

        public void setAmountShow(String amountShow) {
            this.amountShow = amountShow;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    private UIATransfer uiaTransfer = null;

    public UIATransfer getUiaTransfer() {
        return uiaTransfer;
    }

    public void setUiaTransfer(UIATransfer uiaTransfer) {
        this.uiaTransfer = uiaTransfer;
    }
}
