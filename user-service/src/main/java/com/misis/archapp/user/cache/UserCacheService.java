package com.misis.archapp.user.service.cache;

import com.misis.archapp.user.dto.UserDTO;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String CACHE_PREFIX = "user:v1:";
    private static final Duration TTL = Duration.ofSeconds(60);

    public Optional<UserDTO> getFromCache(UUID id) {
        return Optional.ofNullable((UserDTO) redisTemplate.opsForValue().get(CACHE_PREFIX + id));
    }

    public void saveToCache(UserDTO user) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + user.id(), user, TTL);
    }

    public void removeFromCache(UUID id) {
        redisTemplate.delete(CACHE_PREFIX + id);
    }

}
