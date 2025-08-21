package com.Lsrs.gateway.filters;

import com.example.Until.JwtUntil;
import com.example.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class LoginCheckfilters implements GlobalFilter, Ordered {
    @Autowired
   private RedisService redisService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null) {
            return unauthorized(exchange, "Missing token");
        }

        try {
            // 解析 Token（会抛出 ExpiredJwtException 如果过期）
            Claims claims = JwtUntil.parseJwt(token);
            String uuid = claims.get("uuid").toString();

            // 检查 Redis 中 Token 是否有效（例如是否被手动吊销）
            if (!isValidToken(uuid)) {
                return unauthorized(exchange, "Token revoked");
            }
        } catch (ExpiredJwtException e) {
            return unauthorized(exchange, "Token expired");  // 捕获过期异常
        } catch (Exception e) {
            return unauthorized(exchange, "Invalid token");  // 捕获其他异常（如签名错误、格式错误）
        }

        return chain.filter(exchange);
    }

    // 检查路径是否匹配白名单
    private boolean isWhitelisted(String path) {
        List<String> whitelist = new ArrayList<>();
        whitelist.add("/api/weixin/**");
        whitelist.add("/api/resource/seat"); // 添加 WebSocket 路径到白名单// 登录接口
        return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    // Token 校验逻辑（示例）
    private boolean isValidToken(String uuid) {
        // 实现你的 Token 校验逻辑（例如 JWT 验证）
        String s = redisService.get("uuid:"+uuid);//open_ID 如果为空则可能失效或着未注册
        if (s == null) {
            return false;
        }
        return true;
    }
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Authenticate", "Bearer error=\"invalid_token\", error_description=\"" + message + "\"");
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(("{\"code\":401, \"message\":\"" + message + "\"}").getBytes())));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // 最高优先级
    }
} 