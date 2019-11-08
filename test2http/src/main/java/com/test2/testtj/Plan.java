package com.test2.testtj;

import com.google.gson.annotations.SerializedName;

public class Plan {
    @SerializedName("planid")
    private String planID;

    @SerializedName("planname")
    private String planName;

    /**
     * 体检计划创建日期
     */

    @SerializedName("createdate")
    private String createDate;

    /**
     * 体检计划状态（过滤条件）:1 未完成 ,2 进行中, 3 已结束
     */
    @SerializedName("state")
    private String state;

    /**
     * 体检地址
     */
    @SerializedName("addr")
    private String addr;

    /**
     * 离线模式下，判断是否下载该计划内数据的标识
     */
    private boolean isDownloaded;
}
