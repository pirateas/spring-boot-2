package com.yty.boot2.dao;

import com.yty.boot2.domain.entity.TestEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yangtianyu
 */
@Repository
public interface TestMapper {

    /**
     * create
     *
     * @param testEntity 参数
     */
    void create(TestEntity testEntity);

    /**
     * find
     *
     * @param id id
     * @return 结果
     */
    TestEntity findById(Long id);
}
