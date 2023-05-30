package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController {

    @Reference
    private HouseUserService houseUserService;
    //去添加的页面
    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId, Map map)
    {
        //将房源id放到request域中
        map.put("houseId",houseId);

        return "houseUser/create";
    }
    //添加
    @RequestMapping("/save")
    public String save(HouseUser houseUser)
    {
        //调用houseuserservice
        houseUserService.insert(houseUser);
        return "common/successPage";
    }
    //去修改的页面
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Long id,Map map)
    {
        HouseUser houseUserServiceById = houseUserService.getById(id);

        map.put("houseUser",houseUserServiceById);
        return "houseUser/edit";
    }

    @RequestMapping("/update")
    public String update(HouseUser houseUser)
    {
        //调用
        houseUserService.update(houseUser);
        return "common/successPage";
    }

    @RequestMapping("/delete/{houseId}/{houseUserId}")
    public String delete(@PathVariable("houseId")Long houseId,@PathVariable("houseUserId") Long houseUserId)
    {
        houseUserService.delete(houseUserId);
        //重定向到查询房源详情的方法
        return "redirect:/house/"+houseId;
    }
}
