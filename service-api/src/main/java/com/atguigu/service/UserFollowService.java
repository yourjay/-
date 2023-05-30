package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow>{
    void follow(Long id, Long houseId);

    //查看是否关注房源
    boolean isFollowed(Long id, Long id1);

    //分页查询我的关注
    PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long id);

    //取消关注房源
    void cancelFollowed(Long id);
}
