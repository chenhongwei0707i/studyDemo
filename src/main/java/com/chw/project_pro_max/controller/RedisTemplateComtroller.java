package com.chw.project_pro_max.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@EnableSwagger2
@Api( tags = "redis使用")
public class RedisTemplateComtroller {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判断key是否存在
     */
    @GetMapping("stringRedisTemplate")
    @ApiOperation("判断key是否存在")
    @ApiImplicitParams({@ApiImplicitParam(name = "key", value = "key")})
    public boolean stringRedisTemplateHasKey(String key) {
        return stringRedisTemplate.hasKey(key);

    }

    /**
     * 判断key是否存在
     */
    @GetMapping("haskey")
    @ApiOperation("判断key是否存在")
    @ApiImplicitParams({@ApiImplicitParam(name = "key", value = "key")})
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 指定key的失效时间
     */
    @GetMapping("expire")
    @ApiOperation("指定key的失效时间")
    @ApiImplicitParams({@ApiImplicitParam(name = "key", value = "key")})
    public void expire(String key, long time) {
        //参数一：key
        //参数二：睡眠时间
        //参数三：睡眠时间单位 TimeUnit.DAYS 天 TimeUnit.HOURS 小时 。。。
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 根据key获取过期时间
     */
    @GetMapping("getexpire")
    @ApiOperation("根据key获取过期时间")
    @ApiImplicitParams({@ApiImplicitParam(name = "key", value = "key")})
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire;
    }

    /**
     * 根据key删除reids中缓存数据
     */
    @GetMapping("delredisbykey")
    @ApiOperation("根据key删除reids中缓存数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "key", value = "key")})
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 保存和读取String
     */
    @GetMapping("stringredisdemo")
    @ApiOperation("保存和读取String")
    public String stringredisdemo() {
        //设置过期时间为1分钟
        redisTemplate.opsForValue().set("key1", "沙发斯蒂芬啥地方", 1, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("key2", "value2");
        redisTemplate.opsForValue().set("key3", "value3");
        //读取redis数据
        String result1 = redisTemplate.opsForValue().get("key1").toString();
        String result2 = redisTemplate.opsForValue().get("key2").toString();
        String result3 = redisTemplate.opsForValue().get("key3").toString();
        System.out.println("缓存结果为：result：" + result1 + "  " + result2 + "   " + result3);
        return "缓存结果为：result：" + result1 + "  " + result2 + "   " + result3;
    }

    /**
     * 保存和读取list
     */
    @GetMapping("listredisdemo")
    public String listredisdemo() {
        List<String> list1 = new ArrayList<>();
        list1.add("a1");
        list1.add("a2");
        list1.add("a3");
        List<String> list2 = new ArrayList<>();
        list2.add("b1");
        list2.add("b2");
        list2.add("b3");
        Long listkey1 = redisTemplate.opsForList().leftPushAll("listkey1", list1);
        Long listkey2 =  redisTemplate.opsForList().rightPushAll("listkey2", list2);
        System.out.println(listkey1 + "=====" +listkey2);
        List<String> resultList1 = (List<String>) redisTemplate.opsForList().leftPop("listkey1");
        List<String> resultList2 = (List<String>) redisTemplate.opsForList().rightPop("listkey2");
        System.out.println("resultList1:" + resultList1);
        System.out.println("resultList2:" + resultList2);
        return "成功";
    }

    /**
     * Hash结构，保存和读取map
     */
    @GetMapping("mapredisdemo")
    public String mapredisdemo() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        map.put("key5", "value5");
        redisTemplate.opsForHash().putAll("map1", map);
        Map<String, String> resultMap = redisTemplate.opsForHash().entries("map1");
        List<String> reslutMapList = redisTemplate.opsForHash().values("map1");
        Set<String> resultMapSet = redisTemplate.opsForHash().keys("map1");
        String value = (String) redisTemplate.opsForHash().get("map1", "key1");
        System.out.println("value:" + value);
        System.out.println("resultMapSet:" + resultMapSet);
        System.out.println("resultMap:" + resultMap);
        System.out.println("resulreslutMapListtMap:" + reslutMapList);
        return "成功";
    }

    /**
     * 保存和读取Set
     */
    @GetMapping("setredisdemo")
    public String getredisdemo() {
        SetOperations<String, String> set = redisTemplate.opsForSet();
        set.add("key1", "value1");
        set.add("key1", "value2");
        set.add("key1", "value3");
        Set<String> resultSet = redisTemplate.opsForSet().members("key1");
        System.out.println("resultSet:" + resultSet);
        return "resultSet:" + resultSet;
    }
}
