package com.xc.mail.jedis.test;

import com.xc.mail.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class Redis02SpringbootApplicationTests {

   /* @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void contextLoads(){

        redisTemplate.opsForValue().set("key","111",30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().append("","");
        redisTemplate.opsForList();
        RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
        connection.flushDb();
        System.out.println(redisTemplate.opsForValue().get("key"));
    }*/

}
