package com.hepengju.mockdata.web;

import com.hepengju.mockdata.common.JsonR;
import com.hepengju.mockdata.core.GeneratorMeta;
import com.hepengju.mockdata.core.GeneratorParam;
import com.hepengju.mockdata.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(tags = "生成数据")
@RestController
@RequestMapping("/")
public class GeneratorController {

    @Autowired private GeneratorService genService;

    @ApiOperation("获取生成器")
    @GetMapping("getGenMap")
    public JsonR<Map<String, List<GeneratorMeta>>> getGenMap(){
        return JsonR.ok().addData(genService.getGenMap());
    }

    @ApiOperation("刷新表格")
    @PostMapping("refreshTable")
    public JsonR<List<Map<String,String>>> refreshTable(@RequestBody GeneratorParam param) {
        return JsonR.ok().addData(genService.refreshTable(param));
    }

    @ApiOperation("下载表格")
    @PostMapping("downTable")
    public void downTable(@RequestBody GeneratorParam param) {
        genService.downTable(param);
    }

    // -------------------- 微信接口 --------------------
    @ApiOperation("获取数据")
    @GetMapping("getData")
    public JsonR<List<String>> getData(@RequestParam String name, @RequestParam(defaultValue = "5") int sampleSize) {
        return JsonR.ok().addData(genService.getData(name, sampleSize));
    }

    @ApiOperation("获取可用生成器名称")
    @GetMapping("getGenNameList")
    public JsonR<Set<String>> getGenNameList() {
        return JsonR.ok().addData(genService.getMetaMap().keySet());
    }


}
