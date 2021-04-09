package com.xc.mail.repository;

import com.xc.mail.pojo.GoodsStore;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GoodsStoreRepository extends JpaRepository<GoodsStore,Integer>, JpaSpecificationExecutor<GoodsStore> {

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update goods_store gs set gs.store=?2 where gs.code=?1")
    Integer updateStore(@Param("code") String code, @Param("store") Integer store);


    @Query(value = "select * from goods_store gs where gs.code=code",nativeQuery = true)
    GoodsStore getGoodsStore(@Param("code") String code);
}
