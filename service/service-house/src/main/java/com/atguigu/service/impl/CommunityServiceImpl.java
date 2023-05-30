package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.CommunityDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.BaseService;
import com.atguigu.service.CommnityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CommnityService.class)
@Transactional
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommnityService {

    @Autowired
    private CommunityDao communityDao;
    @Autowired
    private DictDao dictDao;
    @Override
    protected BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    //重写分页的方法，目的是给小区中的区域和板块的名字赋值
    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

        PageHelper.startPage(pageNum, pageSize);
        //调用dao
        Page<Community> page = communityDao.findPage(filters);
        //遍历page对象
        for (Community community : page) {
                    //根据区域的id获取区域的名字
            String areaName = dictDao.getNameById(community.getAreaId());
            //根据板块的id获取板块的名字
            String plateName = dictDao.getNameById(community.getPlateId());

            community.setAreaName(areaName);

            community.setPlateName(plateName);


        }
        return new PageInfo<>(page,10);
    }

    @Override
    public List<Community> findAll() {
        return communityDao.findAll();
    }

    @Override
    public Community getById(Serializable id) {
        Community community = communityDao.getById(id);
        //根据区域的id获取区域的名字
        String areaName = dictDao.getNameById(community.getAreaId());
        //根据板块的id获取板块的名字
        String plateName = dictDao.getNameById(community.getPlateId());

        community.setAreaName(areaName);

        community.setPlateName(plateName);
        return community;

    }
}
