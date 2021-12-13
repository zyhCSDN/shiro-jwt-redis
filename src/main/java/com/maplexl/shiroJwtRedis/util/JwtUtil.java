package com.maplexl.shiroJwtRedis.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.maplexl.shiroJwtRedis.constant.CommonConstant;
import com.maplexl.shiroJwtRedis.pojo.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * JWT工具类
 *
 * @author 枫叶
 * @date 2020/11/1
 */
@Slf4j
public class JwtUtil {

    /**
     * 生成签名,5min后过期
     */
    public static long EXPIRE_TIME;
    /**
     * RefreshToken到期时间为，秒为单位 30min
     */
    public static long REFRESH_EXPIRE_TIME;
    /**
     * 密钥盐
     */
    private static String TOKEN_SECRET;

    /**
     * 设置token过期时间及密钥盐
     *
     * @param expireTime        客户端token过期时间
     * @param refreshExpireTime 服务器token过期时间
     * @param tokenSecret       token加密使用的盐值
     */
    public static void setProperties(long expireTime, long refreshExpireTime, String tokenSecret) {
        JwtUtil.EXPIRE_TIME = expireTime;
        JwtUtil.REFRESH_EXPIRE_TIME = refreshExpireTime;
        JwtUtil.TOKEN_SECRET = tokenSecret;
    }

    /**
     * 生成签名,5min后过期
     *
     * @param userId 用户名
     * @return 加密的token
     */
    public static String sign(Long userId) {
        long currentTime = System.currentTimeMillis();
        log.error("当前系统时间currentTime:{}", currentTime);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CommonConstant.JWT_PERMISSIONS_KEY, null);
//        claims.put(CommonConstant.JWT_ROLES_KEY, null);
//        claims.put(CommonConstant.JWT_USER_NAME, "AdminJwt");
        String token = null;
            //设置头信息
            HashMap<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            Date expireAt = new Date(currentTime + EXPIRE_TIME);
            token = JWT.create()
                    .withHeader(header)
                    //存放数据
                    .withClaim("userId", userId)
                    .withClaim("currentTime", currentTime)
                    //token过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
            log.info("客户端token过期时间EXPIRE_TIME:{}秒",EXPIRE_TIME/1000);
            log.info(" 服务器token过期时间REFRESH_EXPIRE_TIME:{}秒",REFRESH_EXPIRE_TIME);
            log.info("TOKEN_SECRET:{}",TOKEN_SECRET);
        return token;
    }



    /**
     * 校验token 第一种方法
     * @param token
     * @return
     */
    public static boolean checkSign(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("授权认证通过");
            log.info("userId:    [{}]", jwt.getClaim("userId").asLong());
            Date expiresAt = jwt.getExpiresAt();
            log.info("过期时间： [{}]", SimpleDateFormat.getDateTimeInstance().format(expiresAt));
            return true;
        } catch (JWTVerificationException exception) {
           return false;
        }
    }
    /**
     * 还有一种方案。jwt的token仅仅作为redis的key或者token内的载体存储redis的key，token本身不设置expire time，
     * 给redis的key设置有效期，例如30分钟，每次需要携带token请求不同的服务，都更新redis中这个key的时间，这样就类似session的有效期自动刷新了，这个key 30分钟不使用，自动删除，
     * 一直使用每次有后延30分钟。即做到了不会反复重签token到前端的问题，也避免了需要多个令牌来维系权限验证的问题呢
     * <p>
     * jwt自动续期  实现原理
     * jwt token自动续期的实现原理如下：登录成功后将用户生成的 jwt token 作为key、value
     * 存储到cache缓存里面 (这时候key、value值一样)，将redis缓存有效期设置为 jwt token有效时间的2倍。
     * 当该用户再次请求时，通过后端的一个 jwt Filter 校验前端传的token是否是有效token，如果没有传递token或者token错误表明是非法请求，直接抛出异常即可；
     * 根据规则取出cache token，判断cache token是否存在，此时主要分以下几种情况：
     * cache token 不存在  这种情况表明该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * cache token 存在，则需要使用jwt工具类验证该cache token 是否过期超时，不过期无需处理。
     * <p>
     * 过期则表示该用户一直在操作只是token失效了，后端程序会给token对应的key映射的value值重新生成jwt token并覆盖value值，该缓存生命周期重新计算。
     *
     * @param token 原令牌
     */
    public static boolean refreshToken(String token) {
        String redisToken = String.valueOf(RedisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (CommonUtils.isNotEmpty(redisToken)) {
            // 校验sign token有效性
            if (!checkSign(token)) {
                //已过期
                String newToken = sign(getUserId(token));
                //redis设置新的token为30分钟过期，签名token其实还是五分钟
                RedisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newToken,REFRESH_EXPIRE_TIME);
                log.info("redis原token令牌：{}", token);
                log.info("token过期新生成的token令牌：{}", newToken);
            }
            return true;
        }
        return false;
    }

    /**
     * 获得token中的信息
     *
     * @return token中包含的用户id
     */
    public static long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException e) {
            return -1L;
        }
    }
    /**
     * 获得token中的信息
     *
     * @return token中包含的用户id
     */
    public static User getUserInfo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Long userId = jwt.getClaim("userId").asLong();
            Object o = RedisUtil.get(CommonConstant.PREFIX_USER + userId);
            User user = JSONObject.parseObject(JSONObject.toJSONString(o), User.class);
            return user;
        } catch (JWTDecodeException e) {
            return new User();
        }
    }

    /**
     * 获取currentTime
     *
     * @param token 密钥
     * @return currentTime
     */
    public static Long getCurrentTime(String token) {
        try {
            DecodedJWT decodedjwt = JWT.decode(token);
            return decodedjwt.getClaim("currentTime").asLong();
        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * 根据request中的token获取currentTime
     *
     * @param request request
     * @return CurrentTime
     */
    public static Long getCurrentTime(HttpServletRequest request) {
        String accessToken = request.getHeader("token");
        Long currentTime = getCurrentTime(accessToken);
        if (currentTime == null) {
            throw new RuntimeException("未获取到currentTime");
        }
        return currentTime;
    }


    /**
     * 根据request中的token获取账号
     *
     * @param request request
     * @return 用户的账号
     */
    public static Long getUserId(HttpServletRequest request) {
        String accessToken = request.getHeader("token");
        return getUserId(accessToken);
    }


//
//    public static void main(String[] args) {
//        Map<String, Object> claims = new HashMap<String, Object>();
//        claims.put("username", "zss");
//        claims.put("age", 18);
//        String secret = "a1g2y47dg3dj59fjhhsd7cnewy73j";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        // 生成jwt访问令牌
//        String token = Jwts.builder()
//                // 用户
//                .setClaims(claims)
//                // 主题 - 存用户名
//                .setSubject("张三")
//                .setId("666")  //登录用户的id
//                .setSubject("小马")  //登录用户的名称
//                .setExpiration(new Date(System.currentTimeMillis() + 30*1000))//过期时间
//                .setIssuedAt(new Date(System.currentTimeMillis()))//当前时间
//                // 加密算法和密钥
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//        System.out.println("token令牌是："+token);
//        Claims claims1 = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//
//        Date d1 = claims1.getIssuedAt();
//        Date d2 = claims1.getExpiration();
//        System.out.println("username参数值：" + claims1.get("username"));
//        System.out.println("登录用户的id：" + claims1.getId());
//        System.out.println("登录用户的名称：" + claims1.getSubject());
//        System.out.println("令牌签发时间：" + sdf.format(d1));
//        System.out.println("令牌过期时间：" + sdf.format(d2));
//    }
}
