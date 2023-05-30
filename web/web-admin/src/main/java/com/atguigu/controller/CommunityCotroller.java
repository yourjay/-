package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommnityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityCotroller extends BaseController{

    @Reference
    private CommnityService commnityService;

    @Reference
    private DictService dictService;
    //分页及带条件查询的方法
    @PreAuthorize("hasAuthority('community.show')")
    @RequestMapping
    public String index(Map map, HttpServletRequest request){

    //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将参数放入域中
        map.put("filters",filters);
        //调用分页方法
        PageInfo<Community> pageInfo = commnityService.findPage(filters);
        //将pageInfo放到request域中
        map.put("page",pageInfo);
        //根据编码获取北京所有区
        List<Dict> areaList = dictService.getListByDictCode("beijing");
        //将北京所有的区域放到request域中
        map.put("areaList",areaList);
        return "community/index.html";
    }
    //去添加小区的页面
    @PreAuthorize("hasAuthority('community.create')")
    @RequestMapping("/create")
    public String goAddPage(Map map)
    {
        //根据编码获取北京所有区
        List<Dict> areaList = dictService.getListByDictCode("beijing");
        //将北京所有的区域放到request域中
        map.put("areaList",areaList);
        return "community/create";
    }
    //添加小区
    @PreAuthorize("hasAuthority('community.create')")
    @RequestMapping("/save")
    public String save(Community community)
    {
        //调用service
        commnityService.insert(community);
        //去成功页面
        return "common/successPage";
    }
    //去修改小区的页面
    @PreAuthorize("hasAuthority('community.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(Map map,@PathVariable("id") Long id)
    { //根据编码获取北京所有区
        List<Dict> areaList = dictService.getListByDictCode("beijing");
        //将北京所有的区域放到request域中
        map.put("areaList",areaList);
        Community community = commnityService.getById(id);
        map.put("community",community);
        return "community/edit";
    }
    @PreAuthorize("hasAuthority('community.edit')")
    @RequestMapping("/update")
    public String update(Community community)
    {
        commnityService.update(community);
        return "common/successPage";
    }
    //删除
    @PreAuthorize("hasAuthority('community.delete')")
    @RequestMapping("delete/{id}")
    public String delete(@PathVariable("id")Long id)
    {
        commnityService.delete(id);
        return "redirect:/community";
    }
}
