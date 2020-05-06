package com.swf.attence.mapper;

import com.swf.attence.entity.UserMsg;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto-genergator123
 * @since 2019-02-28
 */
public interface UserMsgMapper extends BaseMapper<UserMsg> {
    List<UserMsg> selectUserMsgAndDeptMsg();
    /*    List<UserMsg> selectUserMsgAndDeptMsgById(Integer id);*/

    UserMsg selectUserMsgAndDeptMsgByUserid(String userid);
}
