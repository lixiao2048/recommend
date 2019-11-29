package com.bingo.qa.common;

import lombok.Data;

/**
 */
@Data
public class CommonResp<T> {
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * response code
     */
    private Integer code;

    /**
     * response message
     */
    private String message;

    /**
     * response data
     */
    private T data;

    /**
     * @param <T>
     * @return
     */
    public static <T> CommonResp<T> buildSuccessResp() {

        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(CommonRespConst.SUCCESS.getCode());
        resp.setMessage(CommonRespConst.SUCCESS.getMsg());
        return resp;
    }

    /**
     * @param data
     * @param <T>
     * @return
     */
    public static <T> CommonResp<T> buildSuccessResp(T data) {
        CommonResp<T> resp = buildSuccessResp();
        resp.setData(data);
        return resp;
    }

    /**
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> CommonResp<T> buildFailResp(String msg) {
        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(CommonRespConst.FAIL.getCode());
        resp.setMessage(msg);
        return resp;
    }
}
