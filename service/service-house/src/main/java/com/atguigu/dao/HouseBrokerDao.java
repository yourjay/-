package com.atguigu.dao;

import com.atguigu.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerDao extends BaseDao<HouseBroker>{

    //根据房源id查询房源的经纪人
    List<HouseBroker> getHouseBrokersByHouseId(Long houseId);
}
