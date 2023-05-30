package com.atguigu.service;

import com.atguigu.entity.Community;

import java.util.List;

public interface CommnityService extends BaseService<Community>{
    List<Community> findAll();
}
