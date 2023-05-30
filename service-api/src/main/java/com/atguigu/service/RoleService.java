package com.atguigu.service;

import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {

    List<Role> findAll();
    //根据用户的id查询用户的角色
    Map<String,Object> findRolesByAdminId(Long adminId);


    void assignRole(Long adminId, Long[] roleIds);
}
