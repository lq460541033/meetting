/*
package com.swf.attence;

import com.swf.attence.service.*;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttenceApplicationExcelTests {
    @Autowired
   private IAttenceMsgService iAttenceMsgService;
    @Autowired
    private AttenceMsgMapper attenceMsgMapper;
    @Autowired
    private IUserMsgService iUserMsgService;
    @Autowired
    private ILeaveMsgService iLeaveMsgService;
    @Autowired
    private IStateControlService iStateControlService;
    @Autowired
    private ICameraMsgService iCameraMsgService;

    @Autowired
    private IEveryTaskService iEveryTaskService;
    @Test
    public void contextLoads() throws IOException, DocumentException, SQLException, ClassNotFoundException {
      */
/*  List<Icommand20190327> icommand20190327s = iIcommand20190327Service.selectList(new EntityWrapper<Icommand20190327>().eq("1", 1));
        for(int i=0;i<icommand20190327s.size();i++){
            Icommand20190327 icommand20190327 = icommand20190327s.get(i);
            if (icommand20190327.getIcommandUsername()== null || icommand20190327.getIcommandUsername().equals("")){
                System.out.println("空的，不用管");
            }else {
                UserMsg userMsg = iUserMsgService.selectOne(new EntityWrapper<UserMsg>().eq("username", icommand20190327.getIcommandUsername()));
                if (userMsg!=null) {
                    String userid = userMsg.getUserid();
                    System.out.println(userid);
                    icommand20190327.setIcommandUserid(userid);
                    iIcommand20190327Service.update(icommand20190327, new EntityWrapper<Icommand20190327>().eq("icommand_username", icommand20190327.getIcommandUsername()));
                    System.out.println("第" + i + 1 + "行更新完成");
                }
            }
        }*//*

    }

}

*/
