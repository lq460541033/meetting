package com.swf.attence.service.impl;

import com.swf.attence.entity.LeaveMsg;
import com.swf.attence.mapper.LeaveMsgMapper;
import com.swf.attence.service.ILeaveMsgService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto-genergator123
 * @since 2019-02-26
 */
@Service
public class LeaveMsgServiceImpl extends ServiceImpl<LeaveMsgMapper, LeaveMsg> implements ILeaveMsgService {
    @Autowired
    private ILeaveMsgService iLeaveMsgService;

    @Override
    public void insertIntoDatabase(String username, String failStart, String failEnd, String description) {
        LeaveMsg leaveMsg = new LeaveMsg();
        String r1 = UUID.randomUUID().toString().replace("-", "");
        leaveMsg.setId(r1);
        leaveMsg.setUsername(username);
        leaveMsg.setFailStart(failStart);
        leaveMsg.setFailEnd(failEnd);
        leaveMsg.setDescription(description);
        insert(leaveMsg);
    }

    @Override
    public LeaveMsg defultLeaveMsg(String username) {
        String r1 = UUID.randomUUID().toString().replace("-", "");
        LeaveMsg leaveMsg = new LeaveMsg();
        leaveMsg.setId(r1);
        leaveMsg.setUsername(username);
        leaveMsg.setAccess(0);
        leaveMsg.setDescription("员工近期没有请假数据");
        leaveMsg.setFailStart("1970-01-01 00:01");
        leaveMsg.setFailEnd("1970-01-01 00:02");
        iLeaveMsgService.insert(leaveMsg);
        return leaveMsg;
    }
}
