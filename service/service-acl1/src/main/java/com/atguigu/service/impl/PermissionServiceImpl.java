package com.atguigu.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.service.BaseService;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override


    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionsByRoleId(Long roleId) {
        //获取所有的权限
        List<Permission> permissionList=permissionDao.findAll();
        //根据角色id查询已分配的权限的权限id
        List<Long> permissionIds=rolePermissionDao.findPermissionIdsByRoleId(roleId);
        //创建返回的List
        List<Map<String, Object>> returnList=new ArrayList<>();

        //遍历所有的权限
        for (Permission permission : permissionList) {
            //判断当前权限的id在不在permissionId中
//         数据的格式  { id:221, pId:22, name:"随意勾选 2-2-1", checked:true},
            //创建一个map
            Map<String, Object> map=new HashMap();
            map.put("id",permission.getId());
            map.put("pId",permission.getParentId());
            map.put("name",permission.getName());
            //判断当前权限的id在不在permissionIds中
            if(permissionIds.contains(permission.getId())){
                //证明该权限已经被分配了
                map.put("checked",true);
            }
            //将map放入返回的list中
            returnList.add(map);


        }
        return returnList;


    }

    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {

        //根据角色id删除已分配的权限
        rolePermissionDao.deletePermissionIdsByRoleId(roleId);

        for (Long permissionId : permissionIds) {
            if(permissionId!=null)
            {
                //调用rolePermissionDao中保存权限id和角色id的方法
                rolePermissionDao.addRoleIdAndPermission(roleId,permissionId);
            }


        }
    }

    @Override
    public List<Permission> getMenuPermissionByAdminId(Long userId) {
        List<Permission> permissionList=null;
        //判断是否是超级管理员
        if(userId==1)
        {
            //证明是超级管理员,获取所有的权限
            permissionList=permissionDao.findAll();

        }
        else{
            //根据用户的id查询权限的菜单
            permissionList =permissionDao.getMenuPermissionByAdminId(userId);
        }
        //通过permissionHelper工具类
        List<Permission> treeList = PermissionHelper.bulid(permissionList);
        return treeList;
    }

    @Override
    public List<Permission> findAllMenu() {

        List<Permission> all = permissionDao.findAll();
       if( CollectionUtils.isEmpty(all)) return null;
        List<Permission> result = PermissionHelper.bulid(all);

        return result;
    }

    @Override
    public List<String> getPermissionCodeByAdminId(Long id) {
        List<String> permissionCodes=null;
        if(id==1)
        {
            //证明是系统管理员
            permissionCodes=permissionDao.getAllPermissionCodes();

        }else{
            //根据用户id查询权限码
            permissionCodes=permissionDao.getPermissionCodesByAdminId(id);
        }
        return permissionCodes;
    }
}
