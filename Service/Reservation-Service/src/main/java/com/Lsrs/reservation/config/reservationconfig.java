package com.Lsrs.reservation.config;


import org.springframework.context.annotation.Configuration;


@Configuration //标注配置类  声明一个类为配置类，用于取代bean.xml配置文件注册bean对象。

public class reservationconfig {
    //重发组件  默认间隔100ms *1.5  5次 最大1s
//    @Bean
// Retryer retryer(){
//    return new Retryer.Default();
// }

}
