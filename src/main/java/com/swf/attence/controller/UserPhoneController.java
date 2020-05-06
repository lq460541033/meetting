package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.DeptMsg;
import com.swf.attence.entity.UserPhone;
import com.swf.attence.service.IUserPhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
@Controller
public class UserPhoneController {
    @Autowired
    private IUserPhoneService iUserPhoneService;

    private static final Logger logger = LoggerFactory.getLogger(UserPhoneController.class);
    /**
     * 分页列出所有事务
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/userPhones",method =GET)
    public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize, Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserPhone> userPhones = iUserPhoneService.selectList(new EntityWrapper<UserPhone>().eq("1", 1));
        System.out.println(userPhones);
        PageInfo<UserPhone> userPhonesPageInfo = new PageInfo<>(userPhones, 5);
        model.addAttribute("userPhonesPageInfo",userPhonesPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",userPhonesPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",userPhonesPageInfo.getPages());
        model.addAttribute("isLastPage",userPhonesPageInfo.isIsLastPage());
        return "userPhoneControl/userPhoneList";
    }

    /**
     * 跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPhone/{id}")
    public String toUpdateTime(@PathVariable("id")Integer id,Model model){
        UserPhone userPhone = iUserPhoneService.selectById(id);
        model.addAttribute("userPhone",userPhone);
        return "userPhoneControl/userPhoneAdd";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @GetMapping("/userPhone")
    public String toInsertTime(){
        return "userPhoneControl/userPhoneAdd";
    }

    /**
     * 添加一个事务
     * @param userPhone
     * @return
     */
    @PostMapping("/userPhone")
    public String insertTime(UserPhone userPhone){
        iUserPhoneService.insert(userPhone);
        logger.info(userPhone+"添加成功");
        return "redirect:/userPhones";
    }

    /**
     * 修改一个事务
     * @param userPhone
     * @return
     */
    @PutMapping("/userPhone")
    public String updateTime(UserPhone userPhone){
        iUserPhoneService.updateById(userPhone);
        logger.info(userPhone+"修改成功");
        return "redirect:/userPhones";
    }
    @DeleteMapping("/userPhone/{id}")
    public String deleteTime(@PathVariable("id") Integer id){
        iUserPhoneService.deleteById(id);
        logger.info(id+"删除成功");
        return "redirect:/userPhones";
    }
    
}
