package com.atguigu.service;

import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission>{

    List<Map<String, Object>> findPermissionsByRoleId(Long roleId);

    void assignPermission(Long roleId, Long[] permissionIds);

    List<Permission> getMenuPermissionByAdminId(Long userId);
    List<Permission> findAllMenu();

    List<String> getPermissionCodeByAdminId(Long id);
}
