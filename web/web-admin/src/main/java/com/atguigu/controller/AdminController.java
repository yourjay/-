package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{

    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    //注入密码加密器
    @Autowired
    private PasswordEncoder passwordEncoder;
    //分页及带条件的查询
    @PreAuthorize("hasAuthority('admin.show')")
    @RequestMapping
    public String findPage(Map map, HttpServletRequest request)
    {
        //获取请求参数
        Map<String, Object> filters = getFilters(request);
        //将filters放到request域中
        map.put("filters",filters);
        //调用AdminService中的分页方法
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        //将pageInfo对象梵高request域中
        map.put("page",pageInfo);


        return "admin/index.html";


    }
    //去用户添加的页面
    @PreAuthorize("hasAuthority('admin.create')")
    @RequestMapping("/create")
    public String goAddCreate()
    {
        return "admin/create";
    }
    //保存用户
    @PreAuthorize("hasAuthority('admin.create')")
    @RequestMapping("/save")
    public String save(Admin admin)
    {
        //对amdin对象的密码进行加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        //调用adminservice中保存的方法

        adminService.insert(admin);

        return "common/successPage";
    }
    //删除用户
    @PreAuthorize("hasAuthority('admin.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id)
    {
        adminService.delete(id);
        return "redirect:/admin";
    }

    //去更新的页面
    @PreAuthorize("hasAuthority('admin.edit')")
    @RequestMapping("/edit/{adminId}")
    public String goEditPage(@PathVariable("adminId")Long adminId,Map map){
        //获取要修改的对象

        Admin admin = adminService.getById(adminId);
        //把对象放到域中
        map.put("admin",admin);

        return "admin/edit";

    }
    @PreAuthorize("hasAuthority('admin.edit')")
    @RequestMapping("/update")
    public String uptate(Admin admin)
    {
        adminService.update(admin);

        return "commmon/successPage";
    }

    //去上传头像的页面
    @PreAuthorize("hasAuthority('admin.edit')")
    @RequestMapping("/uploadShow/{id}")
    public String goUploadPage(@PathVariable("id") Long id,Map map)
    {
        //将用户的id放入request域中
        map.put("id",id);
        return "admin/upload";

    }
    //上传
    @PreAuthorize("hasAuthority('admin.edit')")
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id, MultipartFile file)
    {
        try {
            //根据用户的id查询用户
            Admin admin = adminService.getById(id);
            //获取字节数组
            byte[] bytes = file.getBytes();
            //随机生成一个文件名
            String fileName = UUID.randomUUID().toString();

            //通过qiniuutil将文件上传到七牛云
            QiniuUtil.upload2Qiniu(bytes,fileName);
            //给用户设置头像地址
            admin.setHeadUrl("http://rtmav1b4e.hn-bkt.clouddn.com/"+fileName);
            //调用AdminService中更新的方法
            adminService.update(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "common/successPage";
    }
    //去分配角色的页面
    @PreAuthorize("hasAuthority('admin.assign')")
    @RequestMapping("/assignShow/{adminId}")
    public String goAssignShowPage(@PathVariable("adminId") Long adminId, ModelMap modelMap)
    {
        //将用户的id放入request域中
        modelMap.addAttribute("adminId",adminId);
        //调用业务逻辑层
        Map<String, Object> rolesByAdminId = roleService.findRolesByAdminId(adminId);

        modelMap.addAllAttributes(rolesByAdminId);
        return "admin/assignShow";
    }

    //分配角色
    @PreAuthorize("hasAuthority('admin.assign')")
    @RequestMapping("/assignRole")
    public String assignRole(Long adminId,Long[] roleIds)
    {
        //调用roleService中分配角色的方法
        roleService.assignRole(adminId,roleIds);
        return "common/successPage";
    }

}
