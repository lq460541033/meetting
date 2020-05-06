package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.StateControl;
import com.swf.attence.service.IStateControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author : white.hou
 * @description :请假啊 这些东西
 * @date: 2019/1/2_21:03
 */
@Controller
public class StateControlController {
    @Autowired
    private IStateControlService iStateControlService;

    private static final Logger logger = LoggerFactory.getLogger(StateControlController.class);

    /**
     * 分页列出所有请假事由
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/stateControls",method =GET)
    public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize, Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<StateControl> stateControls = iStateControlService.selectList(new EntityWrapper<StateControl>().eq("1", 1));
        System.out.println(stateControls);
        PageInfo<StateControl> stateControlsPageInfo = new PageInfo<>(stateControls, 5);
        model.addAttribute("stateControlPageInfo",stateControlsPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",stateControlsPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",stateControlsPageInfo.getPages());
        model.addAttribute("isLastPage",stateControlsPageInfo.isIsLastPage());
        return "stateControl/stateControl_list";
    }

    /**
     * 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/stateControl/{id}")
    public String toUpdateTime(@PathVariable("id")Integer id,Model model){
        StateControl stateControl = iStateControlService.selectById(id);
        model.addAttribute("stateControl",stateControl);
        return "stateControl/stateControl_add";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @GetMapping("/stateControl")
    public String toInsertTime(){
        return "stateControl/stateControl_add";
    }

    /**
     * 添加一个请假事由
     * @param stateControl
     * @return
     */
    @PostMapping("/stateControl")
    public String insertTime(StateControl stateControl,Model model){
        boolean b = iStateControlService.failidExist(stateControl);
        try{
            if (b){
                iStateControlService.insert(stateControl);
                model.addAttribute("msg","添加成功");
                logger.info("添加成功");
            }else {
                model.addAttribute("msg","对不起，该请假事由序号已存在，请检查您的输入");
                logger.info("请假事由序号已存在");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/stateControls";
    }

    /**
     * 修改一个请假事由
     * @param stateControl
     * @return
     */
    @PutMapping("/stateControl")
    public String updateTime(StateControl stateControl){
        iStateControlService.updateById(stateControl);
        logger.info(stateControl+"修改成功");
        return "redirect:/stateControls";
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/stateControl/{id}")
    public String deleteTime(@PathVariable("id") Integer id){
        iStateControlService.deleteById(id);
        logger.info(id+"删除成功");
        return "redirect:/stateControls";
    }
}
