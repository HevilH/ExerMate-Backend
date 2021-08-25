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

    public void setKeyExpTime(String key, long expTime) {
        redisTemplate.expire(key, expTime, TimeUnit.SECONDS);
    }

    public void setKey(String key, Object val) {
        redisTemplate.opsForValue().set(key, val.toString());
    }

    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setMapVal(String key, Object mapKey, Object val) {
        redisTemplate.boundHashOps(key).put(mapKey.toString(), val.toString());
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

    public String getMapVal(String key, String mapKey) {
        return (String) redisTemplate.boundHashOps(key).get(mapKey);
    }

    public int incMapVal(String key, String mapKey, int num) {
        return Math.toIntExact(redisTemplate.boundHashOps(key).increment(mapKey, num));
    }

    public void setMultiMapVal(String key, Object... mapKeyValues) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < mapKeyValues.length - 1; i += 2)
            map.put(mapKeyValues[i].toString(), mapKeyValues[i + 1].toString());
        redisTemplate.boundHashOps(key).putAll(map);
    }

    public String[] getMultiMapVal(String key, String... mapKes) {
        List<Object> objects = redisTemplate.boundHashOps(key).multiGet(Arrays.asList(mapKes));
        String[] ret = new String[objects.size()];
        for (int i = 0; i < objects.size(); ++i) {
            Object obj = objects.get(i);
            if (obj != null)
                ret[i] = obj.toString();
        }
        return ret;
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

    public String getRandSetMember(String key) {
        return redisTemplate.boundSetOps(key).randomMember();
    }

    public boolean isSetMember(String key, String val) {
        return redisTemplate.boundSetOps(key).isMember(val);
    }

    public boolean mapContainKey(String key, Object mapKey) {
        return redisTemplate.boundHashOps(key).hasKey(mapKey.toString());
    }

    public int addToSet(String key, String... val) {
        return (int)(long)redisTemplate.boundSetOps(key).add(val);
    }

    public long removeFromSet(String key, String val) {
        return redisTemplate.boundSetOps(key).remove(val);
    }
}

