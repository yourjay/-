package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.github.pagehelper.StringUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;
    //用户登录时，会调用该方法，并将用户名传入该方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin =adminService.getAdminByUserName(username);
        if(admin==null)
        {
            throw new UsernameNotFoundException("用户不存在");
        }
        //给用户授权
        //调用permissionservice中获取当前用户的权限码
        List<String> permissionCodes=permissionService.getPermissionCodeByAdminId(admin.getId());
        //创建一个list
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        //遍历得到每一个权限码
        for (String permissionCode : permissionCodes) {
            if(!StringUtil.isEmpty(permissionCode))
            {
                System.out.println(permissionCode);
                //创建grantedAuthority对象
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permissionCode);
                //将对象放入集合里
                grantedAuthorities.add(simpleGrantedAuthority);

            }

        }
//        return new User(username,admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        return new User(username,admin.getPassword(), grantedAuthorities);
    }
}
