package com.example.redis;
import com.example.pojo.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis服务
 *
 * @author
 */
@Service
public class RedisService implements IRedis {


    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String key, String value, int validTime) {
        redisTemplate.opsForValue().set(key, value, validTime, TimeUnit.SECONDS);
    }

    @Override
    public void expire(String key, int validTime) {
        redisTemplate.expire(key, validTime, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }


    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Integer append(String key, String str) {
        return redisTemplate.opsForValue().append(key, str);
    }

    @Override
    public List<String> getList(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Long len(String key) {
        return redisTemplate.opsForValue().size(key);
    }


    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String hGet(String key, String field) {
        Object object = redisTemplate.opsForHash().get(key, field);
        if (object == null) {
            return null;
        }
        return (String) object;
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        Map<Object, Object> hGetAll = redisTemplate.opsForHash().entries(key);
        return convert(hGetAll);
    }


    @Override
    public void hSet(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public void hDel(String key, List<String> fields) {
        redisTemplate.opsForHash().delete(key, fields.toArray());
    }

    @Override
    public void hSet(String key, Map<String, String> hash) {
        redisTemplate.opsForHash().putAll(key, hash);
    }

    @Override
    public Boolean hasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public Long hIncrByLong(String key, String field, Long value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public Double hIncrByDouble(String key, String field, Double value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public Integer hSize(String key) {
        return redisTemplate.opsForHash().size(key).intValue();
    }

    @Override
    public void lPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void lPush(String key, List<String> values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public void lrPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public void lrPush(String key, List<String> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public void lSet(String key, Long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void lRem(String key, long count, String value) {
        redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public String lrPop(String key) {

        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public String lIndex(String key, Long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Integer lLen(String key) {
        return Objects.requireNonNull(redisTemplate.opsForList().size(key)).intValue();
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void lSet(String key, Integer index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void sAdd(String key, List<String> values) {
        redisTemplate.opsForSet().add(key, String.valueOf(values));
    }

    @Override
    public void sRem(String key, List<String> values) {
        redisTemplate.opsForSet().remove(key,values.toArray());
    }

    @Override
    public String sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    @Override
    public Set<String> sDiff(List<String> keys) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().difference(key, keys);
    }

    @Override
    public Long sDiffStore(List<String> keys, String storeKey) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().differenceAndStore(key, keys, storeKey);
    }

    @Override
    public Set<String> sInter(List<String> keys) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().intersect(key, keys);
    }

    @Override
    public Long sInterStore(List<String> keys, String storeKey) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().intersectAndStore(key, keys, storeKey);
    }

    @Override
    public Set<String> sUnion(List<String> keys) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().union(key, keys);
    }

    @Override
    public Long sUnionStore(List<String> keys, String storeKey) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().unionAndStore(key, keys, storeKey);
    }


    @Override
    public Integer sSize(String key) {
        return Objects.requireNonNull(redisTemplate.opsForSet().size(key)).intValue();
    }

    @Override
    public Boolean sIsMember(String key, String member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    @Override
    public String sRandMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public Set<String> sMembersAll(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    private Map<String, String> convert(Map<Object, Object> objectMap) {
        Map<String, String> stringMap = new HashMap<>();
        objectMap.keySet().forEach(it -> {
            stringMap.put(String.valueOf(it), String.valueOf(objectMap.get(it)));
        });
        return stringMap;
    }

}
