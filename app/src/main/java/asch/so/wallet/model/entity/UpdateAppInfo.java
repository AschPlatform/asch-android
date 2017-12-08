package asch.so.wallet.model.entity;

import com.vector.update_app.UpdateAppBean;

/**
 * Created by kimziv on 2017/12/8.
 */

public class UpdateAppInfo extends UpdateAppBean {

    private String version_name;
    private int version_code;

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }
}
