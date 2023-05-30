package com.atguigu.dao;

import com.atguigu.entity.Dict;

import java.util.List;

public interface DictDao {
    //根据父id获取该节点下所有的子节点
    public List<Dict> findListByParentId(Long id);

    //根据父id判断该节点是否是父节点
    public Integer isParentNode(Long id);

    //根据编码获取dict对象
    Dict getDictByDictCode(String dictCode);

    //根据id获取名字
    String getNameById(Long id);
}
