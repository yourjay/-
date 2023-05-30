package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    DictService dictService;

    //根据编码获取子节点
    @RequestMapping("/findListByDictCode/{dictCode}")
    public Result findListByDictCode(@PathVariable("dictCode") String dictCode)
    {
        //调用逻辑层
        List<Dict> listByDictCode = dictService.getListByDictCode(dictCode);

        return Result.ok(listByDictCode);
    }
    //根据父id查询所有的子节点
    @RequestMapping("/findListByParentId/{areaId}")
    public Result findListByParentId(@PathVariable("areaId") Long areaId)
    {
        //调用逻辑层
        List<Dict> listByParentId = dictService.getListByParentId(areaId);
        return Result.ok(listByParentId);
    }
}
