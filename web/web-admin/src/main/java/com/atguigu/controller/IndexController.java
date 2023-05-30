package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;
    //去首页
//    @RequestMapping("/")
//    public String index(){
//
//        return "frame/index.html";
//    }
    @RequestMapping("/")
    public String index(Map map){
        //设置默认的id
//        Long userId=1L;
//        Admin admin = adminService.getById(userId);
        //通过springsecurity获取user对象
       User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //调用根据用户名获取admin对象的方法
        Admin admin = adminService.getAdminByUserName(user.getUsername());
        //调用查询用户权限的方法
        List<Permission> permissionList=permissionService.getMenuPermissionByAdminId(admin.getId());
        ///将用户和用户的权限菜单放入域中
        map.put("admin",admin);
        map.put("permissionList",permissionList);
        return "frame/index.html";
    }
    //去主页面
    @RequestMapping("/main")
    public String main(){

        return "frame/main";
    }

    //去登录页面的方法
    @RequestMapping("/login")
    public  String login()
    {
        return "frame/login";
    }

    //去没有权限的页面
    @RequestMapping("/auth")
    public String auth(){
        return "frame/auth";
    }

//    //登出
//    @RequestMapping("/logout")
//    public String logout(HttpSession session)
//    {
//        //将session域中的admin对象移除(失效)
//        session.invalidate();
//        //重定向到登录的请求
//        return "redirect:/login";
//
//    }
}
