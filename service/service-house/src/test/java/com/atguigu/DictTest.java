package com.atguigu;

import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
@RunWith(SpringRunner.class)
public class DictTest {
    @Autowired
    private DictDao dictDao;

    @Test
    public void test(){
        List<Dict> listByParentId = dictDao.findListByParentId(1L);
        for (Dict dict : listByParentId) {
            System.out.println("dict = " + dict);
        }

    }

}
