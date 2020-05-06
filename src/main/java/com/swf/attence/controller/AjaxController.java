package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swf.attence.entity.*;
import com.swf.attence.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.swf.attence.service.impl.UserMsgServiceImpl.ATTENCEDATA;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
/**
 * @author : white.hou
 * @description : ajax controller
 * @date: 2019/3/31_17:27
 */

@RestController
public class AjaxController {
    @Autowired
    private IUserMsgService iUserMsgService;
    @Autowired
    private ICameraMsgService iCameraMsgService;
    @Autowired
    private IDeptMsgService iDeptMsgService;
    @Autowired
    private ILeaveMsgService iLeaveMsgService;
    @Autowired
    private IMeetingCountService iMeetingCountService;


    private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);


    /**
     *
     * @param userid
     * @param cameraidIn
     * @param cameraidOut
     * @return
     */
    @RequestMapping(value = "/attenceMsg/check",method = POST)
    @ResponseBody
    public Map<String,String> checkAttenceMsg(@RequestParam("userid") String userid, @RequestParam("cameraidIn") String cameraidIn, @RequestParam("cameraidOut") String cameraidOut){
        UserMsg userMsg = iUserMsgService.selectOne(new EntityWrapper<UserMsg>().eq("userid", userid));
        CameraMsg msg = iCameraMsgService.selectOne(new EntityWrapper<CameraMsg>().eq("cameraid", cameraidIn));
        CameraMsg msg1 = iCameraMsgService.selectOne(new EntityWrapper<CameraMsg>().eq("cameraid", cameraidOut));
        DeptMsg deptMsg = iDeptMsgService.selectOne(new EntityWrapper<DeptMsg>().eq("deptid", userMsg.getDeptid()));
        HashMap<String, String> stringStringHashMap = new HashMap<>(16);
        stringStringHashMap.put("useridMsg",userMsg.getUserid());
        stringStringHashMap.put("userNameMsg",userMsg.getUsername());
        stringStringHashMap.put("deptMsg",deptMsg.getDeptname());
        stringStringHashMap.put("cameraIn",msg.getCameraPosition());
        stringStringHashMap.put("cameraOut",msg1.getCameraPosition());
        return stringStringHashMap;
    }

    /**
     *
     * @param day
     * @param num
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/generateEveryReports",method = POST)
    @ResponseBody
    public String generateReports(@RequestParam("day") String day,@RequestParam("num")int num) throws IOException {
        String message;
        String name=null;
        if(num==1) {
            name="会议成功";
        }else if (num==2){
            name="迟到";
        }else if (num==3){
            name="早退";
        }else if(num==4){
            name="迟到早退";
        }else if (num==5){
            name="缺勤";
        }
        if (iUserMsgService.generateEveryDayMsg(day,num)){
            message="杭州仰天信息科技"+day+name+".xlsx";
            logger.info(message);
            return message;
        }else {
            message="未知错误，请重试";
            logger.info(message);
            return message;
        }
    }


    /**
     *
     * @param day
     * @param num
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/generateWeekReports",method = POST)
    @ResponseBody
    public String generateWeekReports(@RequestParam("day") String day,@RequestParam(value = "num",defaultValue = "1") int num) throws IOException, ParseException {
        String message;
        String name=null;
        if(num==1) {
            name="会议成功";
        }else if (num==2){
            name="迟到";
        }else if (num==3){
            name="早退";
        }else if(num==4){
            name="迟到早退";
        }else if (num==5){
            name="缺勤";
        }
        if (iUserMsgService.generateEveryWeekMsg(day,num)){
            Map<String, String> map = iUserMsgService.getWeekStartAndEnd(day);
            message="杭州仰天信息科技" +map.get("start")+"-----"+map.get("end") + ".xlsx";
            logger.info(message);
            return message;
        }else {
            message="未知错误，请重试";
            logger.info(message);
            return message;
        }
    }

    /**
     *
     * @param id
     * @param day
     * @return
     */
    @RequestMapping(value = "/acess",method = POST)
    @ResponseBody
    public String acess(@RequestParam("id") String id,@RequestParam("day") String day){
        LeaveMsg leaveMsg = iLeaveMsgService.selectById(id);
        leaveMsg.setAccess(1);
        iLeaveMsgService.update(leaveMsg,new EntityWrapper<LeaveMsg>().eq("id",id));
        MeetingCount meetingCount =iMeetingCountService .selectOne(new EntityWrapper<MeetingCount>().like("acess_time", day + "%"));
        meetingCount.setFailId(1);
        iMeetingCountService.update(meetingCount,new EntityWrapper<MeetingCount>().eq("id",meetingCount.getId()));
        logger.info("已通过该用户请求");
        return "true";
    }


    /**
     *
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/download",method = POST)
    @ResponseBody
    public String downloadFile(HttpServletRequest request,
                               HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
        // 如果文件名不为空，则进行下载
        if (fileName != null) {
            //设置文件路径
            File file = new File(ATTENCEDATA, fileName);
            // 如果文件名存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        os.flush();
                        i = bis.read(buffer);
                    }
                    System.out.println("下载成功");
                }
                catch (Exception e) {
                    System.out.println("下载失败");
                }
                finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";
    }


    /**
     * 判断要添加或者修改的数据是否有效
     * @return
     */
    @RequestMapping(value = "/checkUserPhone",method = POST)
    @ResponseBody
    public String checkUserPhone(@RequestParam("userid") String userid,@RequestParam("username") String username){
        UserMsg userMsg = iUserMsgService.selectOne(new EntityWrapper<UserMsg>().eq("userid", userid));
        if (userMsg!=null){
            if (username.equals(userMsg.getUsername())){
                return "true";
            }else {
                return "false";
            }
        }
        return "false";
    }
}
