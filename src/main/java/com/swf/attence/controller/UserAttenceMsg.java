package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.CameraMsg;
import com.swf.attence.entity.LeaveMsg;
import com.swf.attence.entity.StateControl;
import com.swf.attence.service.ILeaveMsgService;
import com.swf.attence.service.IStateControlService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.event.MouseListener;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author : white.hou
 * @description : 用户请假模块 用户control
 * @date: 2019/2/28_16:03
 */
@Controller
public class UserAttenceMsg {
    @Autowired
    private ILeaveMsgService iLeaveMsgService;
    @Autowired
    private IStateControlService iStateControlService;
    private static final Logger logger = LoggerFactory.getLogger(UserAttenceMsg.class);
    /**
     * 分页列出所有设备
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/userAttenceMsgs",method =GET)
    public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,Model model) {
        String adminIdSession = (String)SecurityUtils.getSubject().getSession().getAttribute("adminIdSession");
        PageHelper.startPage(pageNum, pageSize);
        List<LeaveMsg> leaveMsgs = iLeaveMsgService.selectList(new EntityWrapper<LeaveMsg>().eq("username", adminIdSession));
        logger.info("查出的个人请假数据为： "+leaveMsgs);
        System.out.println(leaveMsgs);
        PageInfo<LeaveMsg> leaveMsgPageInfo = new PageInfo<>(leaveMsgs, 5);
        model.addAttribute("leaveMsgPageInfo",leaveMsgPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",leaveMsgPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",leaveMsgPageInfo.getPages());
        model.addAttribute("isLastPage",leaveMsgPageInfo.isIsLastPage());
        return "leave/leaveMsg_list";
    }
    @GetMapping("/userLeave")
    public String userLeave(Model model){
        List<StateControl> stateControls = iStateControlService.selectList(new EntityWrapper<StateControl>().eq("1", 1));
        model.addAttribute("stateControls",stateControls);
        return "leave/leaveMsg_add";
    }

    @PostMapping("/addUserAttenceMsg")
    public String addUserAttenceMsg(String failStart,String failEnd,String descritption){
        String username =(String) SecurityUtils.getSubject().getSession().getAttribute("adminIdSession");
        iLeaveMsgService.insertIntoDatabase(username,failStart,failEnd,descritption);
        logger.info(username+"新增请假信息 :"+failStart+failEnd+descritption+"添加 成功");
        return "redirect:/userAttenceMsgs";
    }
}
