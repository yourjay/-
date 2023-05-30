package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseSerice houseSerice;
    @Reference
    private CommnityService commnityService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private UserFollowService userFollowService;

    //分页及带条件查询的方法
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findPageList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize")Integer pageSize,
                               @RequestBody HouseQueryVo houseQueryVo)
    {
        //调用前端页面及带条件查询的方法
        PageInfo<HouseVo> pageInfo=houseSerice.findPageList(pageNum,pageSize,houseQueryVo);
        return Result.ok(pageInfo);
    }

    //查询房源详情
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id , HttpSession session)
    {
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

        //调用houseservice中查询房源的方法
        House house = houseSerice.getById(id);
        System.out.println(house.getBuildArea());
        //获取小区信息
        Community community = commnityService.getById(house.getCommunityId());
        System.out.println(community.getBuildYears());
        //获取房源图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImageByHouseIdAndType(id, 1);
        //获取房源的经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokersByHouseId(id);
        for (HouseBroker houseBroker : houseBrokerList) {
            System.out.println("houseBroker = " + houseBroker);
        }
        //创建一个map
        Map map=new HashMap<>();
        //将房源信息、小区信息、房源图片、经纪人放到map中
        map.put("house",house);
        map.put("community ",community);
        map.put("houseImage1List",houseImage1List);
        map.put("houseBrokerList",houseBrokerList);
        //设置默认没有关注房源

        //设置一个变量
        boolean isFollowed=false;
//        map.put("isFollow",false);
        //从session域中获取UserInfo对象
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        if(userInfo !=null)
        {
            isFollowed= userFollowService.isFollowed(userInfo.getId(),id);
        }
        //将isfollowed放入map中
        map.put("isFollow",isFollowed);
        return Result.ok(map);

    }
}
