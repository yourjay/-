package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Reference
    private DictService dictService;


    @Override
    protected BaseDao<UserFollow> getEntityDao() {
        return userFollowDao;
    }

    @Override
    public void follow(Long id, Long houseId) {
        //创建一个对象
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(id);
        userFollow.setHouseId(houseId);

        //调用dao
        userFollowDao.insert(userFollow);



    }

    @Override
    public boolean isFollowed(Long user_id, Long house_id) {

        //调用dao
        Integer count=userFollowDao.getCountByUserIdAndHouseId(user_id,house_id);
        if(count>0)
        {
            //已经关注该房源
            return true;
        }
        else{
            return false;
        }





    }

    @Override
    public PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long userId) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);

        Page<UserFollowVo> page=userFollowDao.findPageList(userId);

        for (UserFollowVo userFollowVo : page) {
            //获取房屋的户型
            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            //获取楼层
            String floorName=dictService.getNameById(userFollowVo.getFloorId());

            //获取朝向
            String directionName = dictService.getNameById(userFollowVo.getDirectionId());
            userFollowVo.setHouseTypeName(houseTypeName);
            userFollowVo.setFloorName(floorName);
            userFollowVo.setDirectionName(directionName);
        }
        return new PageInfo<>(page,5);
    }

    @Override
    public void cancelFollowed(Long id) {

        userFollowDao.delete(id);
    }
}
