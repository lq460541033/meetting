package com.swf.attence.service;

import com.swf.attence.entity.TimeControl;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
public interface ITimeControlService extends IService<TimeControl> {
    /**
     * 查出员工每天应该到的时间
     * @param day
     * @return
     */
     String userShoudBeInTime(String day);

    /**
     * 查出员工每天应该出的时间
     * @param day
     * @return
     */
     String userShoudBeOutTime(String day);
}
