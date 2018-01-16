package so.asch.sdk.transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kimziv on 2018/1/16.
 */

public class ArgsInfo {

    private ArrayList<String> argsList;


    public ArgsInfo() {
        this.argsList = new ArrayList<>();
    }
    public ArgsInfo(String[] args) {
        this.argsList = new ArrayList<>(Arrays.asList(args));
    }

    public void addArg(String arg){
        argsList.add(arg);
    }


    public String getArgsJson(){
        StringBuilder builder=new StringBuilder();
        builder.append("[");
        for (String arg :
                argsList) {
            builder.append(String.format("\"%s\"",arg));
            if (!arg.equals(argsList.get(argsList.size()-1))){
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
