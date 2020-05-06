package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.DeptMsg;
import com.swf.attence.entity.LeaveMsg;
import com.swf.attence.entity.UserMsg;
import com.swf.attence.service.ICameraMsgService;
import com.swf.attence.service.IDeptMsgService;
import com.swf.attence.service.ILeaveMsgService;
import com.swf.attence.service.IUserMsgService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author : white.hou
 * @description :用户信息控制类
 * @date: 2019/1/3_10:30
 */
@Controller
public class UserMsgController {
    @Autowired
    private IUserMsgService iUserMsgService;
    @Autowired
    private IDeptMsgService iDeptMsgService;
    @Autowired
    private ICameraMsgService iCameraMsgService;
    @Autowired
    private ILeaveMsgService iLeaveMsgService;

    private static final Logger logger = LoggerFactory.getLogger(UserMsgController.class);
    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/userMsgs",method =GET)
   public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize, Model model){
       PageHelper.startPage(pageNum,pageSize);
        List list = iUserMsgService.selectUserMsgAndDeptMsg();
       PageInfo pageInfo = new PageInfo<>(list, 5);
       model.addAttribute("pageInfo",pageInfo);
       model.addAttribute("pageNum",pageNum);
       model.addAttribute("pageSize",pageSize);
       model.addAttribute("isFirstPage",pageInfo.isIsFirstPage());
       model.addAttribute("totalPages",pageInfo.getPages());
       model.addAttribute("isLastPage",pageInfo.isIsLastPage());
       return "userMsgControl/userMsg_list";
   }

    /**
     * 跳转添加用户页面
     * @return
     */
   @GetMapping("/userMsg")
   public String toInsertUserMsg(Model model){
       List<DeptMsg> deptMsgs = iDeptMsgService.selectList(new EntityWrapper<DeptMsg>().eq("1", 1));
       model.addAttribute("deptMsgs",deptMsgs);
       System.out.println(deptMsgs);
       return "userMsgControl/userMsg_add";
   }

    /**
     * 编辑用户 跳转到编辑（添加）页面
     * @param id
     * @param model
     * @return
     */
   @GetMapping("/userMsg/{id}")
   public String toUpdateUserMsg(@PathVariable("id") Integer id, Model model){
       UserMsg userMsg = iUserMsgService.selectById(id);
       model.addAttribute("userMsg",userMsg);
       List<DeptMsg> deptMsgs = iDeptMsgService.selectList(new EntityWrapper<DeptMsg>().eq("1", 1));
       model.addAttribute("deptMsgs",deptMsgs);
       System.out.println(deptMsgs);
       return "userMsgControl/userMsg_add";
   }

    /**
     * 添加单个用户
     * @param userMsg
     * @return
     */
   @PostMapping("/userMsg")
   public String insertUserMsg(UserMsg userMsg) throws IOException {
       iUserMsgService.insert(userMsg);
       iCameraMsgService.uploadUserPicAndUserMessageByOne(userMsg);
       logger.info(userMsg+"添加成功");
       return "redirect:/userMsgs";
   }

    /**
     * 修改单个用户
     * @param userMsg
     * @return
     */
    @PutMapping("/userMsg")
    public String updateUserMsg(UserMsg userMsg){
        iUserMsgService.updateById(userMsg);
        logger.info(userMsg+"修改成功");
        return "redirect:/userMsgs";
    }

    /**
     * 删除指定员工
     * @param id
     * @return
     */
   @DeleteMapping("/userMsg/{id}")
   public  String deleteUserMsg(@PathVariable("id") Integer id,Model model){
      /* UserMsg userMsg = iUserMsgService.selectById(id);*/
       iUserMsgService.delImgFromUserpic(id);
       iUserMsgService.deleteById(id);
       logger.info(id+"删除成功");
       /*model.addAttribute("deleteUserMsg","该员工——工号: "+userMsg.getUserid()+" 姓名： "+userMsg.getUsername()+" 个人图片: "+userMsg.getUserpic()+"已删除完成");*/
       return "redirect:/userMsgs";
   }

    /**
     * 查看用户当日请假信息
     * @param username
     * @return
     */
    @RequestMapping(value = "/getUserLeaveMsg")
    public String getUserLeaveMsg(@RequestParam(value = "username") String username, @RequestParam(value = "day") String day, Model model){
        LeaveMsg leaveMsg = iLeaveMsgService.selectOne(new EntityWrapper<LeaveMsg>().eq("username", username).andNew().le("fail_start", day + "%").andNew().ge("fail_end", day + "%"));
        logger.info("查出"+username+"相关会议信息是: "+leaveMsg);
        if (leaveMsg!=null){
          model.addAttribute("leaveMsg",leaveMsg);
          UserMsg userMsg = iUserMsgService.selectOne(new EntityWrapper<UserMsg>().eq("userid", leaveMsg.getUsername()));
          model.addAttribute("userid",userMsg.getUsername());
          Session session = SecurityUtils.getSubject().getSession();
          session.setAttribute("failDay",day);
        }else {
            LeaveMsg msg = iLeaveMsgService.defultLeaveMsg(username);
            model.addAttribute("leaveMsg",msg);
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("failDay",day);
        }
        return "userMsgControl/user_attence";
    }


}
