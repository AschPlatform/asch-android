package asch.io.wallet.model.entity;

/**
 * Created by Deng on 2018 09 25
 */

public class WithdrawAsset {

        String gateway;
        String symbol;
        String desc;
        int precision;
        String revoked;
        String _version_;

        public String getGateway() {
            return gateway;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public String getRevoked() {
            return revoked;
        }

        public void setRevoked(String revoked) {
            this.revoked = revoked;
        }

        public String get_version_() {
            return _version_;
        }

        public void set_version_(String _version_) {
            this._version_ = _version_;
        }


}
