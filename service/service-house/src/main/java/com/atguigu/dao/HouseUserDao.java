package com.atguigu.dao;

import com.atguigu.entity.HouseUser;

import java.util.List;

public interface HouseUserDao extends BaseDao<HouseUser>{
    //根据房源id查询房东信息
    List<HouseUser> getHouseUsersByHouseId(Long houseId);
}
