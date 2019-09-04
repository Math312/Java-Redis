package com.jllsq.common.sds.exception;

import java.io.UnsupportedEncodingException;

public class SDSMaxLengthException extends Exception{

    private static final String EXCEPTION_FORMAT="SDSMaxLengthException - the string is longer to 512MB, string: %s";

    private String tip;

    public SDSMaxLengthException(byte[] content){
        super();
        this.tip = new String(content);
    }

    public SDSMaxLengthException(byte[] content,String charSet) throws UnsupportedEncodingException {
        super();
        this.tip = new String(content,charSet);
    }

    @Override
    public String getMessage() {
        String message = String.format(EXCEPTION_FORMAT, this.tip);
        return message+"\n"+super.getMessage();
    }
}
