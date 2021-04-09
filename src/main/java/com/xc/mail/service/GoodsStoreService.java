package com.xc.mail.service;

import com.xc.mail.pojo.GoodsStore;
import io.swagger.models.auth.In;

public interface GoodsStoreService {

    /**
     * 根据产品编号更新库存
     * @param code
     * @return
     */
    String updateGoodsStore(String code,int count);

    /**
     * 获取库存对象
     * @param code
     * @return
     */
    GoodsStore getGoodsStore(String code);

}
