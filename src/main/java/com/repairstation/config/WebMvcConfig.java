package com.repairstation.config;


import com.repairstation.common.JacksonObjectMapper;
import com.repairstation.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Value("${project-config.web-url}")
    private String webUrl;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] pathPatterns = {
                "/orders",                  //报修订单
                "/staff/login",             //登录
                "/wxzStatus",               //获取接单状态
                "/wxzStatus/orderTitle",    //报修页公告
                "/wxzStatus/stopTitle",     //停止报修页
                "/common/upload/**",        //上传图片
                "/common/download-photo/**",//下载图片
                "/link",                    //软件工具箱
                "/common/download-zip",     //下载备份文件
                "/staff/csv",
                "/staff/count-csv",
                "/stu/**",
                "/sub-status",
                "/sub/stu",
                "/orders/check-repeat"
        };

        //注册自定义拦截器对象
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(pathPatterns);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有路径
                .allowedOrigins(webUrl) // 允许的来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的头部信息
                .allowCredentials(true) // 是否允许发送Cookie
                .maxAge(3600); // 预检请求的有效期，单位秒
    }

    /**
     * 扩展mvc消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }
}
