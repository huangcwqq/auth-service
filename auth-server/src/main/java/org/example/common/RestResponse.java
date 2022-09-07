package org.example.common;

public class RestResponse<D> {

    private static final int CODE_OK = 0;
    private static final String MSG_OK = "OK";

    private int code;
    private String msg;
    private D data;

    private RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private RestResponse(int code, String msg, D data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <Data> RestResponse<Data> ok() {
        return new RestResponse<>(CODE_OK, MSG_OK);
    }

    public static <Data> RestResponse<Data> ok(Data data) {
        return new RestResponse<>(CODE_OK, MSG_OK, data);
    }

    public static <Data> RestResponse<Data> ok(int code, String msg, Data data) {
        return new RestResponse<>(code, msg, data);
    }

    public static RestResponse<Void> error(int code, String msg) {
        return new RestResponse<>(code, msg);
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public D getData() {
        return this.data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(D data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder
                .append("code:")
                .append(code)
                .append(",msg")
                .append(msg)
                .append(",data")
                .append(data)
                .toString();
    }
}
