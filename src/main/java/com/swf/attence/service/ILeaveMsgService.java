package com.swf.attence.service;

import com.swf.attence.entity.LeaveMsg;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto-genergator123
 * @since 2019-02-26
 */
public interface ILeaveMsgService extends IService<LeaveMsg> {
    /**
     * 添加请假信息
     * @param username
     * @param failStart
     * @param failEnd
     * @param description
     */
   void insertIntoDatabase(String username,String failStart,String failEnd,String description);

    /**
     * 构造一个默认的LeaveMsg
     */
   LeaveMsg defultLeaveMsg(String username);
}
