package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.MeetingCount;
import com.swf.attence.service.IMeetingCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 在这里开启一个新的会议
 */
@Controller
public class MeetingCountController {
    @Autowired
    private IMeetingCountService iMeetingCountService;

    private static final Logger logger = LoggerFactory.getLogger(MeetingCountController.class);

    /**
     * 分页展示正式会议信息
     * @param pageNum
     * @param pageSize
     * @param model
     */
    @RequestMapping(value = "/meetingCounts",method = GET)
    public String meetingCountPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize, Model model){
        PageHelper.startPage(pageNum, pageSize);
        List<MeetingCount> meetingCounts = iMeetingCountService.selectList(new EntityWrapper<MeetingCount>().eq("1", 1));
        System.out.println(meetingCounts);
        PageInfo<MeetingCount> meetingCountPageInfo = new PageInfo<>(meetingCounts, 5);
        model.addAttribute("meetingCountPageInfo",meetingCountPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",meetingCountPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",meetingCountPageInfo.getPages());
        model.addAttribute("isLastPage",meetingCountPageInfo.isIsLastPage());
        return "meetingCountControl/meetingCount_list";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @GetMapping("/meetingCount")
    public String toInsert(){
        return "meetingCountControl/meetingCount_add";
    }

    /**
     * 添加用户会议信息
     * @param meetingCount
     * @return
     */
    @PostMapping("/meetingCount")
    public String insertIntoMeetingCount(MeetingCount meetingCount){
        String replace = UUID.randomUUID().toString().replace("-", "");
        meetingCount.setId(replace);
        iMeetingCountService.insert(meetingCount);
        return "redirect:/meetingCounts";
    }

    /**
     * 跳转到生成报表界面
     * @return
     */
    @GetMapping("/generate")
    public String toGeneratReports(){
        return "meetingCountControl/meetingCount_generate";
    }
}
