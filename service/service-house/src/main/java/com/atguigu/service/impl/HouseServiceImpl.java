package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.DictService;
import com.atguigu.service.HouseSerice;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service(interfaceClass = HouseSerice.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseSerice {

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private DictDao dictDao;

    @Override
    protected BaseDao<House> getEntityDao() {


        return houseDao;
    }

    @Override
    public void publish(Long houseId, Integer status) {

        House house = new House();

        //设置id
        house.setId(houseId);
        //设置状态
        house.setStatus(status);
        //调用更新的方法
        houseDao.update(house);
    }

    @Override
    public PageInfo<HouseVo> findPageList(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //调用houseDao中分页的方法
        Page<HouseVo> page=houseDao.findPageList(houseQueryVo);
        //遍历page
        for (HouseVo houseVo : page) {
            //获取房屋的类型
            String houseTypeNmae = dictDao.getNameById(houseVo.getHouseTypeId());
            //获取楼层
            String floorName = dictDao.getNameById(houseVo.getFloorId());
            //获取房屋的朝向
            String directionName = dictDao.getNameById(houseVo.getDirectionId());
            //设置
            houseVo.setHouseTypeName(houseTypeNmae);
            houseVo.setFloorName(floorName);
            houseVo.setDirectionName(directionName);
        }

        return new PageInfo<>(page,5);
    }

//    @Override
////    public House getById(Serializable id) {
////        return houseDao.getById(id);
////    }

    //    重写方法，为了展示房源中的户型等信息
    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        //获取户型

        String houseTypeName = dictDao.getNameById(house.getHouseTypeId());
            //获取楼层
            String floorName = dictDao.getNameById(house.getFloorId());
            //获取朝向
            String directionName = dictDao.getNameById(house.getDecorationId());
            //获取建筑结构
            String buildStructrueName = dictDao.getNameById(house.getBuildStructureId());
            //获取装修情况
            String decorationName = dictDao.getNameById(house.getDecorationId());
            //获取房屋的用途
            String houseUseName = dictDao.getNameById(house.getHouseUseId());
            //设置
            house.setHouseTypeName(houseTypeName);
            house.setFloorName(floorName);
            house.setDirectionName(directionName);
            house.setBuildStructureName(buildStructrueName);
            house.setDecorationName(decorationName);
            house.setHouseUseName(houseUseName);

        return house;
    }
}
