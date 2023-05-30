package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
//查询数据字典
    List<Map<String, Object>> findZnodes(Long id);
    //根据编码获取该节点下的所有子节点
    List<Dict> getListByParentId(Long parentId);
    //根据编码获取该节点下的所有子节点
    List<Dict> getListByDictCode(String dictCode);

    String getNameById(Long id);

}
