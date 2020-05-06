package com.swf.attence.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swf.attence.entity.StateControl;
import com.swf.attence.mapper.StateControlMapper;
import com.swf.attence.service.IStateControlService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StateControlServiceImpl extends ServiceImpl<StateControlMapper, StateControl> implements IStateControlService {
    @Autowired
   private  StateControlMapper stateControlMapper;
    @Override
    public boolean failidExist(StateControl stateControl) {
        StateControl stateControl1 = stateControlMapper.selectOne(stateControl);
        if (stateControl1==null){
            return  true;
        }else {
            return false;
        }
    }

    @Override
    public String dateDiff(int failid) {
        StateControl control = selectOne(new EntityWrapper<StateControl>().eq("failid", failid));
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long diff = control.getCloseTime().getTime() - control.getBeginTime().getTime();
        long day = diff / nd;
        long hour = diff / nh;
        long min = diff / nm;
        return day+"天"+hour+"时";
    }


}
