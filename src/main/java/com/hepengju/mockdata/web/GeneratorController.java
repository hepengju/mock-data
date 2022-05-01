package com.hepengju.mockdata.web;

import com.hepengju.mockdata.core.GeneratorMeta;
import com.hepengju.mockdata.core.GeneratorParam;
import com.hepengju.mockdata.generator.gen400_custom.ScriptGenerator;
import com.hepengju.mockdata.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hepengju.mockdata.core.GeneratorMeta.SAMPLE_SIZE;

@Api(tags = "生成数据")
@RestController
@RequestMapping("/")
public class GeneratorController {

    @Autowired
    GeneratorService genService;

    @ApiOperation("获取生成器")
    @GetMapping("getGenMap")
    public Map<String, List<GeneratorMeta>> getGenMap() {
        return genService.getGenMap();
    }

    @ApiOperation("刷新表格")
    @PostMapping("refreshTable")
    public List<Map<String, String>> refreshTable(@RequestBody GeneratorParam param) {
        return genService.refreshTable(param);
    }

    @ApiOperation("下载表格")
    @PostMapping("downTable")
    public void downTable(@RequestBody GeneratorParam param) {
        genService.downTable(param);
    }

    @ApiOperation("获取数据")
    @GetMapping("getData")
    public List<String> getData(GeneratorMeta meta,
                                @RequestParam(defaultValue = "5") int sampleSize
    ) {
        return genService.getData(meta, sampleSize);
    }

    @ApiOperation("获取10个数据-POST请求")
    @PostMapping("fetchData")
    public List<String> fetchData(@RequestBody GeneratorMeta meta) {
        return genService.getData(meta, SAMPLE_SIZE);
    }

    @ApiOperation("获取可用生成器名称")
    @GetMapping("getGenNameList")
    public Set<String> getGenNameList() {
        return genService.getMetaMap().keySet();
    }

    @ApiOperation("评估脚本")
    @PostMapping("evalScript")
    public List<Object> evalScript(@RequestBody String script) {
        ScriptGenerator gen = new ScriptGenerator();
        gen.setScript(script);
        return gen.generateList(SAMPLE_SIZE);
    }
}
