package com.seji.kidcuiside;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Code {
    final static Pattern pattern = Pattern.compile("(\t+)");
    private String request; //Can be 'save', 'run'
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        String res = "";
        while (code.length() != 0) {
            Matcher mat = pattern.matcher(code);
            if(mat.lookingAt()) {
                res += "\n" + code.substring(0, mat.end());
                code = code.substring(mat.end());
            }
            else {
                res += code.charAt(0);
                code = code.substring(1);
            }
        }
        this.code = res;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
