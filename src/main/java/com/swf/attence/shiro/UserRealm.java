package com.swf.attence.shiro;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swf.attence.entity.AdminMsg;
import com.swf.attence.entity.UserMsg;
import com.swf.attence.service.IUserMsgService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : white.hou
 * @description : 用户Realm配置
 * @date: 2019/2/28_14:29
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserMsgService iUserMsgService;
    /**
     * 资源授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        UserMsg principal = (UserMsg) subject.getPrincipal();
        UserMsg userMsg = iUserMsgService.selectOne(new EntityWrapper<>(principal));
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
        UserMsg userMsg = iUserMsgService.selectOne(new EntityWrapper<UserMsg>().eq("userid", usernamePasswordToken.getUsername()));
        System.out.println(userMsg);
        if (userMsg.getUserid()==null){
            return null;
        }
        return new SimpleAuthenticationInfo(userMsg,userMsg.getUserpassword(),getName());
    }
}
