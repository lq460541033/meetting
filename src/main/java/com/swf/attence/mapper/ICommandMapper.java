package com.swf.attence.mapper;

import com.swf.attence.entity.ICommand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * @author : white.hou
 * @description :报警实体的mapper
 * @date: 2019/2/3_8:44
 */
@Repository
public interface ICommandMapper {
    /**
     * 向数据表插入报警数据
     * @param iCommand
     * @param todayTable
     */
    void insertIntoDatabase(@Param("todayTable") String todayTable,@Param("ic") ICommand iCommand);
}
