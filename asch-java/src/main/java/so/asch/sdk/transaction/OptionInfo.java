package so.asch.sdk.transaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import so.asch.sdk.ContractType;
import so.asch.sdk.TransactionType;

/**
 * Created by kimziv on 2017/10/11.
 */

public class OptionInfo {

    //转账手续费
    private  Long fee;
    //交易类型
    private ContractType type;
    //参数
    //@JSONField
    private String[] args;


    public OptionInfo(Long fee, ContractType type, String[] args) {
        this.fee = fee;
        this.type = type;
        this.args = args;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public ContractType getType() {
        return type;
    }
    public void setType(ContractType type) {
        this.type = type;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getArgsJson(){
        StringBuilder builder=new StringBuilder();
        builder.append("[");
        for (String arg :
                args) {
            builder.append(String.format("\"%s\"",arg));
            if (!arg.equals(args[args.length-1])){
                builder.append(",");
            }
        }
        builder.append("]");
       //String argsJson = JSON.toJSONString(args);
       return builder.toString();
    }
}
