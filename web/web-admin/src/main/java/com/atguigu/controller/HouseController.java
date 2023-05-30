package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController{

    @Reference
    private HouseSerice houseSerice;

    @Reference
    private CommnityService commnityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    //分页及带条件查询的方法
    @PreAuthorize("hasAuthority('house.show')")
    @RequestMapping
    public String index(Map map, HttpServletRequest request)
    {
        //获取所有参数
        Map<String, Object> filters = getFilters(request);
        //将查询到的信息放入域中
        map.put("filters",filters);
        //调用方法开启分页
        PageInfo<House> info = houseSerice.findPage(filters);
        //将分页信息放入域中
        map.put("page",info);

        setRequestAttribute(map);

        return "house/index";
    }
    @PreAuthorize("hasAuthority('house.create')")
    @RequestMapping("/create")
    public String toAddPage(Map map)
    {
        setRequestAttribute(map);
        return "house/create";
    }
    @PreAuthorize("hasAuthority('house.create')")
    @RequestMapping("/save")
    public String save(House house)
    {
        houseSerice.insert(house);
        return "common/successPage";
    }
    @PreAuthorize("hasAuthority('house.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Map map){
        House house = houseSerice.getById(id);
        map.put("house",house);
        setRequestAttribute(map);
        return "house/edit";
    }
    @PreAuthorize("hasAuthority('house.edit')")
    @RequestMapping("/update")
    public String update(House house)
    {
        houseSerice.update(house);
        return "common/successPage";

    }
    @PreAuthorize("hasAuthority('house.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id)
    {
        houseSerice.delete(id);
        return "redirect:/house";
    }



    //获取小区中的信息放入域中
    public void setRequestAttribute(Map map)
    {
        //获取所有的小区
        List<Community> communityList= commnityService.findAll();
        //获取所有户型
        List<Dict> houseTypeList = dictService.getListByDictCode("houseType");
        //获取楼层
        List<Dict> floorList = dictService.getListByDictCode("floor");
        //获取建筑结构
        List<Dict> buildStructureList = dictService.getListByDictCode("buildStructure");
        //获取朝向
        List<Dict> directionList = dictService.getListByDictCode("direction");
        //获取装修情况
        List<Dict> decorationList = dictService.getListByDictCode("decoration");
        //获取访问用途
        List<Dict> houseUseList = dictService.getListByDictCode("houseUse");


        map.put("communityList",communityList);
        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);
    }
    //发布和取消发布

    @RequestMapping("/publish/{houseId}/{status}")
    public String pubilish(@PathVariable("houseId")Long houseId,@PathVariable("status") Integer status)
    {
        //调用方法
        houseSerice.publish(houseId,status);

        return "redirect:/house";
    }
    //查看房源详情
    @PreAuthorize("hasAuthority('house.show')")
    @RequestMapping("/{houseId}")
    public String show(@PathVariable("houseId") Long houseId,Map map)
    {


        House house = houseSerice.getById(houseId);
        map.put("house",house);
        //查询小区
        Community community = commnityService.getById(house.getCommunityId());
        map.put("community",community);

        //查询房源图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImageByHouseIdAndType(houseId,1);
        //查询房产图片
        List<HouseImage> houseImage2List = houseImageService.getHouseImageByHouseIdAndType(houseId,2);
        //查询经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokersByHouseId(houseId);
        //查询房东
        List<HouseUser> houseUserList = houseUserService.getHouseUsersByHouseId(houseId);
        //放到request域中
        map.put("houseImage1List",houseImage1List);
        map.put("houseImage2List",houseImage2List);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseUserList",houseUserList );
        return "house/show";
    }

}
