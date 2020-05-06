package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.DeptMsg;
import com.swf.attence.service.IDeptMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class DeptController {
    @Autowired
    private IDeptMsgService iDeptMsgService;
    
    private static final Logger logger = LoggerFactory.getLogger(TimeControController.class);
    /**
     * 分页列出所有事务
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/deptMsgs",method =GET)
    public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize, Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeptMsg> DeptMsgs = iDeptMsgService.selectList(new EntityWrapper<DeptMsg>().eq("1", 1));
        System.out.println(DeptMsgs);
        PageInfo<DeptMsg> deptMsgsPageInfo = new PageInfo<>(DeptMsgs, 5);
        model.addAttribute("deptMsgPageInfo",deptMsgsPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",deptMsgsPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",deptMsgsPageInfo.getPages());
        model.addAttribute("isLastPage",deptMsgsPageInfo.isIsLastPage());
        return "deptMsg/deptMsg_list";
    }

    /**
     * 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/deptMsg/{id}")
    public String toUpdateTime(@PathVariable("id")Integer id,Model model){
        DeptMsg deptMsg = iDeptMsgService.selectById(id);
        model.addAttribute("deptMsg",deptMsg);
        return "deptMsg/deptMsg_add";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @GetMapping("/deptMsg")
    public String toInsertTime(){
        return "deptMsg/deptMsg_add";
    }

    /**
     * 添加一个事务
     * @param deptMsg
     * @return
     */
    @PostMapping("/deptMsg")
    public String insertTime(DeptMsg deptMsg){
        iDeptMsgService.insert(deptMsg);
        logger.info(deptMsg+"添加成功");
        return "redirect:/deptMsgs";
    }

    /**
     * 修改一个事务
     * @param deptMsg
     * @return
     */
    @PutMapping("/deptMsg")
    public String updateTime(DeptMsg deptMsg){
        iDeptMsgService.updateById(deptMsg);
        logger.info(deptMsg+"修改成功");
        return "redirect:/deptMsgs";
    }
    @DeleteMapping("/deptMsg/{id}")
    public String deleteTime(@PathVariable("id") Integer id){
        iDeptMsgService.deleteById(id);
        logger.info(id+"删除成功");
        return "redirect:/deptMsgs";
    }
}
