package com.zack.znjj.service;

public interface IRedisService {

    void set(String key, Object value);

    Object get(String key);

    void setExpire(String key, Object value, Long time);
}
