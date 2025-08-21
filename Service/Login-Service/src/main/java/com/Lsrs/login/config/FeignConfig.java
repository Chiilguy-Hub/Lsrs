//package com.Lsrs.login.config;
//
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.cloud.openfeign.support.SpringDecoder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import feign.codec.Decoder;
//import java.util.Arrays;
//
//@Configuration
//public class FeignConfig {
//
//    @Bean
//    public Decoder feignDecoder() {
//        return new SpringDecoder(feignHttpMessageConverter());
//    }
//
//    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
//        return () -> {
//            // 创建 Jackson 转换器，并支持 text/plain
//            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//            converter.setSupportedMediaTypes(
//                    Arrays.asList(
//                            MediaType.APPLICATION_JSON,
//                            MediaType.TEXT_PLAIN  // 允许处理 text/plain
//                    )
//            );
//            return new HttpMessageConverters(converter);
//        };
//    }
//}