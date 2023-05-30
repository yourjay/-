package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {

    @Reference
    private AdminService adminService;

    @Reference
    private HouseBrokerService houseBrokerService;
    //去添加经纪人的页面
    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId, Map map)
    {
        //将房源的id放入request域中
        map.put("houseId",houseId);
        //调用方法
        List<Admin> adminList= adminService.findAll();


        map.put("adminList",adminList);

        return "houseBroker/create";
    }
    //保存经纪人
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker)
    {
        //根据经纪人的id查询经纪人的信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());
        //调用houseBrokerService中的保存的方法
        houseBrokerService.insert(houseBroker);

        //成功页面
        return "common/successPage";
    }
    //去修改的页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id,Map map)
    {
        HouseBroker broker = houseBrokerService.getById(id);
        map.put("houseBroker",broker);

        //调用方法
        List<Admin> adminList= adminService.findAll();


        map.put("adminList",adminList);

        return "houseBroker/edit";
    }
    //跟新经纪人
    @RequestMapping("/update")
    public String update(HouseBroker houseBroker)
    {
        //根据经纪人的id查询经纪人的信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());
        //调用更新的方法
        houseBrokerService.update(houseBroker);
        return "common/successPage";
    }

    //删除经纪人
    @RequestMapping("/delete/{houseId}/{brokerId}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("brokerId")Long brokerId)
    {
        //调用删除的方法
        houseBrokerService.delete(brokerId);
        //重定向
        return "redirect:/house/"+houseId;
    }


}
