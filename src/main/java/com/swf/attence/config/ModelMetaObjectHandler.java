package com.swf.attence.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class ModelMetaObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object userpassword = getFieldValByName("userpassword", metaObject);
        if (userpassword==null){
            setFieldValByName("userpassword","fc1709d0a95a6be30bc5926fdb7f22f4",metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object userpassword = getFieldValByName("userpassword", metaObject);
        if (userpassword==null){
            setFieldValByName("userpassword","fc1709d0a95a6be30bc5926fdb7f22f4",metaObject);
        }
    }
}
