package com.hepengju.mockdata;

import com.alibaba.fastjson.JSON;
import com.hepengju.mockdata.common.DataConst;
import org.junit.jupiter.api.Test;

public class YamlToJsonTest {

    @Test
    public void testYamlToJson(){
        System.out.println(DataConst.dict);
        System.out.println(JSON.toJSON(DataConst.dict));
    }
}
