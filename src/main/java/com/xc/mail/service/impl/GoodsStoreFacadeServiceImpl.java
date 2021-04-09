package com.xc.mail.service.impl;

import com.xc.mail.config.RedisLock;
import com.xc.mail.pojo.GoodsStore;
import com.xc.mail.repository.GoodsStoreRepository;
import com.xc.mail.service.GoodsStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class GoodsStoreFacadeServiceImpl implements GoodsStoreService {

    @Resource
    private GoodsStoreRepository goodsStoreRepository;

    @Resource
    private RedisLock redisLock;

    /**
     * 超时时间 5s
     */
    private static final int TIMEOUT = 5*1000;


    /**
     * 根据产品编号更新库存
     * @param code
     * @return
     */
    @Override
    public String updateGoodsStore(String code,int count) {
        //上锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(code, String.valueOf(time))){
            return "排队人数太多，请稍后再试.";
        }
        System.out.println("获得锁的时间戳："+ time);
        try {
            GoodsStore goodsStore = getGoodsStore(code);
            if(goodsStore != null){
                if(goodsStore.getStore() <= 0){
                    return "对不起，卖完了，库存为："+goodsStore.getStore();
                }
                if(goodsStore.getStore() < count){
                    return "对不起，库存不足，库存为："+goodsStore.getStore()+" 您的购买数量为："+count;
                }
                System.out.println("剩余库存："+goodsStore.getStore());
                System.out.println("扣除库存："+count);
                goodsStoreRepository.updateStore(code, count);
                try{
                    //为了更好的测试多线程同时进行库存扣减，在进行数据更新之后先等1秒，让多个线程同时竞争资源
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return "恭喜您，购买成功！";
            }else{
                return "获取库存失败。";
            }
        } finally {
            //释放锁
            redisLock.release(code, String.valueOf(time));
            System.out.println("释放锁的时间戳："+ time);
        }
    }

    /**
     * 获取库存对象
     * @param code
     * @return
     */
    @Override
    public GoodsStore getGoodsStore(String code){
        Optional<GoodsStore> optional = goodsStoreRepository.findById(Integer.valueOf(code));
        return optional.get();
    }

}
