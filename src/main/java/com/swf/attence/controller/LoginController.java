package com.swf.attence.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @author : white.hou
 * @description : 登录的controller
 * @date: 2018/12/30_20:32
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @PostMapping("/login")
    public String login(@RequestParam("adminid") String adminid, @RequestParam("password") String password, Model model){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(adminid, password);
        try {
            subject.login(usernamePasswordToken);
            Session session = subject.getSession();
            session.setAttribute("adminIdSession",adminid);
            logger.info("用户"+adminid+"登录");
            Set<String> realmNames = subject.getPrincipals().getRealmNames();
//           if (realmNames.contains("com.swf.attence.shiro.AdminRealm_1")){
           if (realmNames.contains("com.swf.attence.shiro.UserRealm_1")){
            return "index/index_admin";
           }else {
               return "index/index_user";
           }
        }catch (UnknownAccountException e) {
            /**
             * 登录失败:用户名不存在
             */
            model.addAttribute("msg", "用户名不存在，请校验后登录");
            return "login";
        } catch (IncorrectCredentialsException e) {
            /**
             * 登录失败:密码错误
             */
            model.addAttribute("msg", "密码错误，请重新输入");
            return "login";
        }
    }

    /**
     * 调转控制
     * @return
     */
    @RequestMapping("/tologin")
    public String tologin(){
         return "login";
    }

    /**
     * 权限不足
     * @return
     */
    @RequestMapping("/noAuth")
    public  String noAuth(){
        return "noAuth";
    }

    @RequestMapping("/loginout")
    public String loginOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

}
