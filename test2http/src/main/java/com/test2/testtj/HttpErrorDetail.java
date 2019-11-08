package com.test2.testtj;

/**
 * Created by zhaojian on 2018/2/5.
 */

public class HttpErrorDetail
{
    public static final String HTTP_ERROR_NO_CONNECT = "java.net.ConnectException: Failed to connect";
    public static final String HTTP_ERROR_SERVER_CLOSE = "HTTP 502";
    public static final String HTTP_ERROR_410 = "HTTP 410 Gone";
    public static final String HTTP_ERROR_404 = "404";
    /*账号在其他设备登录*/
    public static final String HTTP_ERROR_401 = "401";
    public static final String HTTP_ERROR_GSON_JSON_SYNTAX_EXCEPTION = "com.google.gson.JsonSyntaxException";
    public static final String HTTP_ERROR_BODY_NULL = "End of input at line 1 column 1";
    public static final String HTTP_ERROR_TIME_OUT = "java.net.SocketTimeoutException: failed to connect to ";

    public static final String SERVER_DATA_NULL = "server_data_null";
    public static final String SERVER_DATA_CODE_NOT_0 = "server_data_code_not_0";


    public String errorCode;
    public String errorMsg;

    public HttpErrorDetail() {
    }

    public HttpErrorDetail(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString()
    {
        return errorCode + "|" + errorMsg;
    }
}
