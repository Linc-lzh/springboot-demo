package com.xs.dynamicthreadpool.component;

import com.xs.dynamicthreadpool.properties.ThreadPoolProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 *
 * 监听器
 */
@Component
public class DtpEventListener {//}  implements ApplicationListener<OrderEvent> {

    // 基于注解的
    @EventListener(DtpEvent.class)
    public void onApplicationEvent(DtpEvent event) {
        ThreadPoolProperties properties = event.getProperties();
        DtpRegistry.refresh(properties.getPoolName(),properties);
    }


}
