package com.pr.exptool.enums;

/**
 * @author wulei
 * @date 2020/10/16
 */

public enum ExpEnum {

    // 检索
    RETRIEVAL("retrieval"),
    // 扭曲矫正
    DEWARP("dewarp"),
    // 去模糊
    DEBLUR("deblur");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ExpEnum(String name) {
        this.name = name;
    }

    public static ExpEnum findByExpName(String expName) {
        for (ExpEnum expEnum : values()) {
            if (expEnum.name.equalsIgnoreCase(expName)) {
                return expEnum;
            }
        }
        return null;
    }
}
