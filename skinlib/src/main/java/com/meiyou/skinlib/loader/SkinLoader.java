package com.meiyou.skinlib.loader;

/**
 * Author: meetyou
 * Date: 17/11/13 17:12.
 */

public enum SkinLoader {
    ASSETS(1),
    SDCARD(2);

    private int nCode;

    public static SkinLoader valueOf(int value) {
        switch(value) {
            case 1:
                return ASSETS;
            case 2:
                return SDCARD;
            default:
                return null;
        }
    }
    public int value() {
        return this.nCode;
    }

    private SkinLoader(int code) {
        this.nCode = code;
    }

}
