package com.ssca.redis;
 
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
public class JedisUtil {
 
    private static String JEDIS_IP;
    private static int JEDIS_PORT;
    private static String JEDIS_PASSWORD;
    //private static String JEDIS_SLAVE;
 
    private static JedisPool jedisPool;
 
    static {
//        Configuration conf = Configuration.getInstance();
//        JEDIS_IP = conf.getString("jedis.ip", null);
//        JEDIS_PORT = conf.getInt("jedis.port", 6379);
//        JEDIS_PASSWORD = conf.getString("jedis.password", null);
    	JEDIS_IP="192.168.2.138";
    	JEDIS_PORT=16379;
    	JEDIS_PASSWORD="lczgywzyy";
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(256);//20
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(60000l);
        config.setTimeBetweenEvictionRunsMillis(3000l);
        config.setNumTestsPerEvictionRun(-1);
        jedisPool = new JedisPool(config, JEDIS_IP, JEDIS_PORT, 10000, JEDIS_PASSWORD, 2);
        

    }
 
    /**
     * ��ȡ���
     * @param key
     * @return
     */
    public static String get(String key) {
 
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
 
        return value;
    }
 
    public static void close(Jedis jedis) {
        try {
            jedisPool.returnResource(jedis);
 
        } catch (Exception e) {
            if (jedis.isConnected()) {
                jedis.quit();
                jedis.disconnect();
            }
        }
    }
 
    /**
     * ��ȡ���
     * 
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {
 
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
 
        return value;
    }
 
    public static void set(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
    }
 
    public static void set(byte[] key, byte[] value, int time) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
    }
 
    public static void hset(byte[] key, byte[] field, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
    }
 
    public static void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
    }
 
    /**
     * ��ȡ���
     * 
     * @param key
     * @return
     */
    public static String hget(String key, String field) {
 
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
 
        return value;
    }
 
    /**
     * ��ȡ���
     * 
     * @param key
     * @return
     */
    public static byte[] hget(byte[] key, byte[] field) {
 
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
 
        return value;
    }
 
    public static void hdel(byte[] key, byte[] field) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
    }
 
    /**
     * �洢REDIS���� ˳��洢
     * @param byte[] key reids����
     * @param byte[] value ��ֵ
     */
    public static void lpush(String key, String value) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
 
        } catch (Exception e) {
 
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //���������ӳ�
            close(jedis);
 
        }
    }
 
    /**
     * �洢REDIS���� ����洢
     * @param byte[] key reids����
     * @param byte[] value ��ֵ
     */
    public static void rpush(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.rpush(key, value);
 
        } catch (Exception e) {
 
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //���������ӳ�
            close(jedis);
 
        }
    }
 
    /**
     * ���б� source �е����һ��Ԫ��(βԪ��)�����������ظ�ͻ���
     * @param byte[] key reids����
     * @param byte[] value ��ֵ
     */
    public static void rpoplpush(byte[] key, byte[] destination) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.rpoplpush(key, destination);
 
        } catch (Exception e) {
 
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //���������ӳ�
            close(jedis);
 
        }
    }
 
    /**
     * ��ȡ�������
     * @param byte[] key ����
     * @return
     */
    public static List<byte[]> lpopList(byte[] key) {
 
        List<byte[]> list = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            list = jedis.lrange(key, 0, -1);
 
        } catch (Exception e) {
 
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //���������ӳ�
            close(jedis);
 
        }
        return list;
    }
 
    /**
     * ��ȡ�������
     * @param byte[] key ����
     * @return
     */
    public static String rpop(String key) {
 
        String bytes = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            bytes = jedis.rpop(key);
 
        } catch (Exception e) {
 
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //���������ӳ�
            close(jedis);
 
        }
        return bytes;
    }
 
   
    public static List<byte[]> lrange(byte[] key, int from, int to) {
        List<byte[]> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, from, to);
 
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //���������ӳ�
            close(jedis);
 
        }
        return result;
    }
 
 
    public static void del(byte[] key) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
    }
 
    public static long llen(byte[] key) {
 
        long len = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            len = jedis.llen(key);
        } catch (Exception e) {
            //�ͷ�redis����
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //���������ӳ�
            close(jedis);
        }
        return len;
    }
 
}