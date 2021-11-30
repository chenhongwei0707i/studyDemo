package com.chw.project_pro_max;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ProjectProMaxApplicationTests {

    @Autowired
     private RedisTemplate<String, String> redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("mykey", "key");
        System.out.println("redisTemplate ======" + redisTemplate.opsForValue().get("mykey"));
    }
}
