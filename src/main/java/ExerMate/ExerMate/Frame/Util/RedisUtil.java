package ExerMate.ExerMate.Frame.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long deleteKeys(String... keys) {
        return redisTemplate.delete(Arrays.asList(keys));
    }

    public void setKey(String key, Object val, long expTime) {
        redisTemplate.opsForValue().set(key, val.toString(), expTime, TimeUnit.SECONDS);
    }


    public Set<String> getMapKeys(String key) {
        Set<Object> srcKeys = redisTemplate.boundHashOps(key).keys();
        Set<String> keys = new HashSet<>();
        for (Object src:srcKeys)
            keys.add(src.toString());
        return keys;
    }

    public Map<Object, Object> getAllMap(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    public boolean setMapValNx(String key, String mapKey, Object val) {
        return redisTemplate.boundHashOps(key).putIfAbsent(mapKey, val.toString());
    }



    public void deleteMapKey(String key, Object... mapKey) {
        String[] strKeys = new String[mapKey.length];
        for (int i = 0; i < mapKey.length; ++i)
            strKeys[i] = mapKey[i].toString();
        redisTemplate.boundHashOps(key).delete(strKeys);
    }

    public Set<String> getSet(String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    public int getSetSize(String key) {
        return (int)(long)redisTemplate.boundSetOps(key).size();
    }

    public Set<String> getSetMembers(String key) {
        return redisTemplate.boundSetOps(key).members();
    }


    public int addToSet(String key, String... val) {
        return (int)(long)redisTemplate.boundSetOps(key).add(val);
    }

    public long removeFromSet(String key, String val) {
        return redisTemplate.boundSetOps(key).remove(val);
    }
}

