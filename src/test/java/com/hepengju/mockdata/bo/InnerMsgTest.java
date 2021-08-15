package com.hepengju.mockdata.bo;

import com.hepengju.mockdata.util.GeneratorUtil;
import com.hepengju.mockdata.util.PrintUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InnerMsgTest {

    @Test void testGeneratorData(){
        String tableName = GeneratorUtil.getTableName(InnerMsg.class);
        List<String> columnNameList = GeneratorUtil.getColumnTitleList(InnerMsg.class);
        List<List<String>> dataList = GeneratorUtil.getDataStringList(InnerMsg.class, 99);
        String printInsert = PrintUtil.printInsert(tableName, columnNameList, dataList, false);
        System.out.println(printInsert);
    }
}
