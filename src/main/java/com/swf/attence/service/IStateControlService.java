package com.swf.attence.service;

import com.swf.attence.entity.CameraMsg;
import com.swf.attence.entity.StateControl;
import com.baomidou.mybatisplus.service.IService;

import java.sql.Timestamp;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
public interface IStateControlService extends IService<StateControl> {
    /**
     * 判断添加的请假序号是否存在，存在返回false。不存在返回true
     * @param stateControl
     * @return
     */
    boolean failidExist(StateControl stateControl);

    /**
     * 计算两个时间的差值
     * @param  failid
     *
     * @return
     */
    String dateDiff(int failid);
}
