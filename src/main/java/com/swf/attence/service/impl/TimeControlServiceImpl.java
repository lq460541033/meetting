package com.swf.attence.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swf.attence.entity.TimeControl;
import com.swf.attence.mapper.TimeControlMapper;
import com.swf.attence.service.ITimeControlService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
@Service
public class TimeControlServiceImpl extends ServiceImpl<TimeControlMapper, TimeControl> implements ITimeControlService {

    @Override
    public String userShoudBeInTime(String day) {
        TimeControl timeControl = selectOne(new EntityWrapper<TimeControl>().eq("things_name", "会议"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        /**
         * 当天应该签到的时间
         */
        String s = simpleDateFormat.format(timeControl.getStartTime());
        return s;
    }

    @Override
    public String userShoudBeOutTime(String day) {
        TimeControl timeControl = selectOne(new EntityWrapper<TimeControl>().eq("things_name", "会议"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        /**
         * 当天应该签退的时间
         */
        String s = simpleDateFormat.format(timeControl.getEndTime());
        return s;
    }
}
