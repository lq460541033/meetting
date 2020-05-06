package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.TimeControl;
import com.swf.attence.service.ITimeControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.acl.LastOwnerException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author : white.hou
 * @description :事务管理
 * @date: 2019/1/1_20:42
 */
@Controller
public class TimeControController {
    @Autowired
    private ITimeControlService iTimeControlService;

    private static final Logger logger = LoggerFactory.getLogger(TimeControController.class);
    /**
     * 分页列出所有事务
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/timeControls",method =GET)
    public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize, Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<TimeControl> timeControls = iTimeControlService.selectList(new EntityWrapper<TimeControl>().eq("1", 1));
        System.out.println(timeControls);
        PageInfo<TimeControl> timeControlsPageInfo = new PageInfo<>(timeControls, 5);
        model.addAttribute("timeControlPageInfo",timeControlsPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",timeControlsPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",timeControlsPageInfo.getPages());
        model.addAttribute("isLastPage",timeControlsPageInfo.isIsLastPage());
        return "timeControl/timeControl_list";
    }

    /**
     * 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/timeControl/{id}")
    public String toUpdateTime(@PathVariable("id")Integer id,Model model){
        TimeControl timeControl = iTimeControlService.selectById(id);
        model.addAttribute("timeControl",timeControl);
        return "timeControl/timeControl_add";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @GetMapping("/timeControl")
    public String toInsertTime(){
        return "timeControl/timeControl_add";
    }

    /**
     * 添加一个事务
     * @param timeControl
     * @return
     */
    @PostMapping("/timeControl")
    public String insertTime(TimeControl timeControl){
        iTimeControlService.insert(timeControl);
        logger.info(timeControl+"添加成功");
        return "redirect:/timeControls";
    }

    /**
     * 修改一个事务
     * @param timeControl
     * @return
     */
    @PutMapping("/timeControl")
    public String updateTime(TimeControl timeControl){
        iTimeControlService.updateById(timeControl);
        logger.info(timeControl+"修改成功");
        return "redirect:/timeControls";
    }
    @DeleteMapping("/timeControl/{id}")
    public String deleteTime(@PathVariable("id") Integer id){
        iTimeControlService.deleteById(id);
        logger.info(id+"删除成功");
        return "redirect:/timeControls";
    }
}
