package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Autowired
    private RoleDao roleDao;
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Map<String, Object> findRolesByAdminId(Long adminId) {
        List<Role> roleList = roleDao.findAll();
        //获取用户已经拥有的角色id
        List<Long> roleIds=adminRoleDao.findRoleIdsByAdminId(adminId);

//        创建两个list,一个存放未选中的角色，一个存放选中的角色
       List<Role> noAssginRoleList=new ArrayList<>();
       List<Role> assginRoleList=new ArrayList<>();
       //遍历所有的角色
        for (Role role : roleList) {
            //判断当前的角色在不在集合中
            if(roleIds.contains(role.getId()))
            {
                //将当前角色放入已选中的集合中
                assginRoleList.add(role);
            }
            else{
                noAssginRoleList.add(role);
            }
        }
        //创建map
        Map<String, Object> roleMap=new HashMap<>();
        roleMap.put("noAssginRoleList",noAssginRoleList);
        roleMap.put("assginRoleList",assginRoleList);
        return roleMap;
    }

    @Override
    public void assignRole(Long adminId, Long[] roleIds) {

        adminRoleDao.deleteRoleIdsByAdminId(adminId);
        //遍历所有角色的id
        for (Long roleId : roleIds) {
            if(roleId != null) {
                //添加用户id和角色id插入到数据库中
                adminRoleDao.addRoleIdAndAdminId(roleId, adminId);
            }

        }
    }


    @Override
    protected BaseDao<Role> getEntityDao() {
        return this.roleDao;
    }
}
