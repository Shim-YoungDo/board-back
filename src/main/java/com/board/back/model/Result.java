package com.board.back.model;

public class Result {

    public enum RESULT_CODE {SUCCESS, FAIL, ERROR}

    private RESULT_CODE resultCode;
    private String resultMessage;

    private Object data;

    public Result (){
        this.resultCode = RESULT_CODE.FAIL;
        this.resultMessage = "";
    }

    public RESULT_CODE getResultCode() {
        return resultCode;
    }

    public void setResultCode(RESULT_CODE resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
