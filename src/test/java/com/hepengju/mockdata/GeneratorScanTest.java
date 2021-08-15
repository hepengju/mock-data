package com.hepengju.mockdata;

import com.hepengju.mockdata.generator.Generator;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 排序测试
 */
public class GeneratorScanTest {

    @Test
    public void testOrder(){
        List<Generator> genList = GeneratorScan.getGeneratorList();
        genList.forEach(g -> System.out.println(g.getClass()));
    }
}
