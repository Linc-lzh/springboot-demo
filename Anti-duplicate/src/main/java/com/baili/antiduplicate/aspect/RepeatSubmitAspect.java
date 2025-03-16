package com.baili.antiduplicate.aspect;

import com.baili.antiduplicate.annotation.RepeatSubmit;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
public class RepeatSubmitAspect {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut(value = "@annotation(repeatSubmit)")
    public void pointCutNoRepeatSubmit(RepeatSubmit repeatSubmit) {

    }

    /**
     * 围绕通知, 围绕着方法执行
     * 两种方式
     * 方式一：IP + 参数 + 类名 + 方法名防重提交
     * 方式二：token + url 检测是否重复提交
     */
    @Around(value = "pointCutNoRepeatSubmit(repeatSubmit)", argNames = "joinPoint,repeatSubmit")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //防重提交类型
        String type = repeatSubmit.limitType().name();
        String key = ":repeat_submit:";
        String url = request.getRequestURI();
        String ipAddress = request.getRemoteAddr();
        if (type.equalsIgnoreCase(RepeatSubmit.Type.PARAM.name())) {
            //方式一，参数 防重提交
            //基于IP 、类名、方法名 和 URL 生成唯一key
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String className = method.getDeclaringClass().getName();
            key = key + String.format("%s-%s-%s-%s", ipAddress, url, className, method);
        } else {
            //方式二，令牌形式防重提交+url
            //从请求头中获取 token，如果不存在，则抛出异常
            String requestToken = request.getHeader("token");
            if (StringUtils.isBlank(requestToken)) {
                log.error("token不存在，非法请求");
                return "token不存在，非法请求";
            }
            key = key + String.format("%s-%s", requestToken, url);
        }
        key = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key)) &&
                stringRedisTemplate.opsForValue().setIfAbsent(key, "", repeatSubmit.lockTime(), TimeUnit.SECONDS)) {

            try {
                //正常执行方法并返回
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("处理异常，请重试！");
                return "处理异常，请重试！";
            }
            finally {
                stringRedisTemplate.delete(key);
            }
        } else {
            // 抛出异常
            log.error("请勿重复提交");
            return "请勿重复提交";
        }
    }
}
