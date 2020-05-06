package com.swf.attence.shiro;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swf.attence.entity.AdminMsg;
import com.swf.attence.service.IAdminMsgService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : white.hou
 * @description :
 * @date: 2018/12/30_20:12
 */
public class AdminRealm extends AuthorizingRealm {
    @Autowired
    private IAdminMsgService iAdminMsgService;

    /**
     * 资源授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        AdminMsg principal =(AdminMsg) subject.getPrincipal();
        AdminMsg adminMsg = iAdminMsgService.selectOne(new EntityWrapper<AdminMsg>().eq("adminid", principal.getAdminid()));
        simpleAuthorizationInfo.addStringPermission(adminMsg.getAdminIdentity());
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)authenticationToken;
        /**
         * 判断接受到的信息
         */
        AdminMsg adminMsg = iAdminMsgService.selectOne(new EntityWrapper<AdminMsg>().eq("adminid", usernamePasswordToken.getUsername()));
        System.out.println(adminMsg);
        if (adminMsg.getAdminid()==null){
            return null;
        }
        return new SimpleAuthenticationInfo(adminMsg,adminMsg.getPassword(),getName());
    }

}
