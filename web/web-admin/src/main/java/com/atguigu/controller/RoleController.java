package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
    public static final String SUCCESS_PAGE="common/successPage";

    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

//    @RequestMapping
//    public String index.html(Map map){
//        //调用roleservice获取所有角色
//
//        List<Role> roleList = roleService.findAll();
//
//        //将所有的角色放到reques域中
//        map.put("list",roleList);
//
//        //去渲染的页面
//        return "role/index.html";
//    }
    //分页及带条件查询方法
    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String index(Map map, HttpServletRequest request)
    {
        //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将filter放入request域中
        map.put("filters",filters);
        //调用roleservice中的分页及待条件查询的对象】
        PageInfo<Role> pageInfo=roleService.findPage(filters);
        //将pageinfo放入域中
        map.put("page",pageInfo);

        return "role/index.html";
    }

    //去添加角色的页面
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/create")
    public String goAddPage(){
        return "role/create";
    }

    //添加角色
    @PreAuthorize("hasAuthority('role.save')")
    @RequestMapping("/save")
    public String save(Role role)
    {
        //调用RoleService中添加的方法
        roleService.insert(role);

        //重定向到查询所有角色的方法
//        return "redirect:/role";
        //去common下的成功页面
        return SUCCESS_PAGE;

    }
    //删除角色
    @PreAuthorize("hasAuthority('role.delete')")//
    @RequestMapping("/delete/{roleId}")
    public  String delete(@PathVariable("roleId") Long roleId){

        //调用roleSerice删除的方法
        roleService.delete(roleId);
        //重定向
        return "redirect:/role";
    }
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/edit/{roleId}")
    public String goEditPage(@PathVariable("roleId")Long roleId,Map map)
    {
        //查询id
        Role role=roleService.getById(roleId);
        //将查询的Role对象放到request域中
        map.put("role",role);

        return "role/edit";

    }
    //更新数据库并回显
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/update")
    public String uptate(Role role)
    {
        roleService.update(role);

        return SUCCESS_PAGE;
    }
    //去分配权限的首页
    @PreAuthorize("hasAuthority('role.assign')")
    @RequestMapping("/assignShow/{roleId}")
    public String goAssignShowPage(@PathVariable("roleId") Long roleId,Map map){
        //将角色id放到request中
        map.put("roleId",roleId);
        //调用根据角色id获取权限的方法
        List<Map<String,Object>> zNodes = permissionService.findPermissionsByRoleId(roleId);
        //将zNodes放到request域中
        map.put("zNodes",zNodes);
        return "role/assignShow";
    }

    //分配权限
    @PreAuthorize("hasAuthority('role.assign')")
    @RequestMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,@RequestParam("permissionIds") Long[] permissionIds)
    {
        //调用分配权限的方法
        permissionService.assignPermission(roleId,permissionIds);
        return "common/successPage";
    }
}
