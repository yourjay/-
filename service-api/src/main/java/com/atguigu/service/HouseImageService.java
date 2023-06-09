package com.atguigu.service;

import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageService extends BaseService<HouseImage>{
    //根据房源id和图片的类型查询房源和房产图片
    List<HouseImage> getHouseImageByHouseIdAndType( Long houseId, Integer type);
}
