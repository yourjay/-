package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping
    public String index(Map map)
    {
        List<Permission> list = permissionService.findAllMenu();

        map.put("list",list);
        return "permission/index";
    }
    //去增加的页面
    @RequestMapping("/create")
    public String create(Map map,Permission permission)
    {
        map.put("permission",permission);


        return "permission/create";
    }
    //实现增加
    @RequestMapping("/save")
    public String save(Permission permission)
    {
         permissionService.insert(permission);
        return "common/successPage";
    }
    //实现删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id)
    {
        permissionService.delete(id);
        return "redirect:/permission";
    }
    //去更新的页面
    @RequestMapping("/edit/{id}")
    public String goUpdate(@PathVariable("id") Long id,Map map)
    {
        Permission permission = permissionService.getById(id);
        map.put("permission",permission);
        return "permission/edit";
    }

    //更新
    @RequestMapping("/update")
    public String update(Permission permission)
    {
        permissionService.update(permission);
        return "common/successPage";
    }
}
