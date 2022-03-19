package com.hepengju.mockdata.common;

import java.util.Arrays;
import java.util.List;

/**
 * 常量类
 */
public interface BaseConst {

    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DATE_FORMAT      = "yyyy-MM-dd";

    List<String> EXCEL_EXT_NAME_LIST = Arrays.asList("xls", "xlsx", "xlsm");

    /**
     * 内置返回代码
     */
    interface ErrorCode {
        int SUCCESS                     = 0;
        int UNKNOWN_ERROR               = -1;
        int NO_LOGIN                    = 10000;
    }
}
