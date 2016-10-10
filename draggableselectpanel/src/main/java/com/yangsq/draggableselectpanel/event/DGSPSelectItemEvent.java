package com.yangsq.draggableselectpanel.event;

/**
 * 选中妆容时的事件
 * @author ysq 2016/9/30.
 */
public class DGSPSelectItemEvent {

    /**
     * 构造函数
     * @param code 选中的itemCode
     * @param groupCode 选中的组code
     */
    public DGSPSelectItemEvent(String code, String groupCode) {
        this.code = code;
        this.groupCode = groupCode;
    }

    private String code;
    private String groupCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
