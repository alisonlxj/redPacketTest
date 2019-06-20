package com.alison.redpackets.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @description DispatcherServlet上下文配置
 */

@Configuration
@EnableWebMvc
@ComponentScan(value = "com.*", includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})})
public class WebConfig {

    @Bean(name = "internalResourceViewResolver")
    public ViewResolver initViewResolver(){
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/jsp");
        internalResourceViewResolver.setSuffix(".jsp");
        return internalResourceViewResolver;
    }

    /**
     * 初始化 RequestMappingHandlerAdapter，并加载HTTP的json格式转换器
     * @return  RequestMappingHandlerAdapter对象
     */
    @Bean(name = "requestMappingHandlerAdapter")
    public HandlerAdapter initRequestMappingHandlerAdapter(){
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        // HTTP JSON转换器
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(mediaType);
        jsonConverter.setSupportedMediaTypes(mediaTypeList);
        requestMappingHandlerAdapter.getMessageConverters().add(jsonConverter);
        return requestMappingHandlerAdapter;
    }


}
