package com.swf.attence.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author : white.hou
 * @description :
 * @date: 2019/1/1_13:51
 */
@Configuration
public class PageConf {
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        // 设置为true 会将RowBounds 第一个参数当成pageNumber使用
        properties.setProperty("offsetAsPageNum", "true");
        // 支持通过mapper参数来传递分页参数
        properties.setProperty("supportMethodsArguments", "true");
        // 使用rowBounds分页会进行count查询
        properties.setProperty("rowBoundsWithCount", "true");
        // 分页参数合理化，启用时如果pageNum<1 查询第一页，pageNum>pages会查询最后一页
        properties.setProperty("reasonable", "true");
        // 设置为true，如果pageSize=0或者RowBounds.limit=0 会查出全部结果，相当于没有执行分页查询，返回结果仍然是pages
        properties.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
