package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {



    @Reference
    private DictService dictService;

//    去展示数据字典页面
    @RequestMapping
    public String index()
    {
        return "dict/index.html";
    }

    //获取数据字典中的数据
    @RequestMapping("/findZnodes")
    @ResponseBody
    public Result findZnodes(@RequestParam(value="id",defaultValue = "0")Long id)
    {
        //调用业务逻辑层
        List<Map<String,Object>> zNodes =dictService.findZnodes(id);

        return Result.ok(zNodes);

    }
    //根据id获取所有子节点
    @RequestMapping("/findListByParentId/{areaId}")
    @ResponseBody
    public Result findListByParentId(@PathVariable("areaId") Long id)
    {
        //调用业务逻辑层
        List<Dict> listByParentId = dictService.getListByParentId(id);

        return Result.ok(listByParentId);
    }
}
