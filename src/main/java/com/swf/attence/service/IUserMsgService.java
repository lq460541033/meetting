package com.swf.attence.service;

import com.swf.attence.entity.UserMsg;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
public interface IUserMsgService extends IService<UserMsg> {

    /**
     * 查
     * @return
     */
    List selectUserMsgAndDeptMsg();
    /**
     * 通过指定id查询用户的所有信息
     * @param id
     * @return
     */
     List selectUserMsgAndDeptMsgById(Integer id);
    /**
     * 通过指定id查询用户的所有信息
     * @param userid
     * @return
     */
    List selectUserMsgAndDeptMsgByUserid(String userid);

    /**
     * 根据用户工号删除图片文件夹指定员工的图片
     * @param id
     */
    void delImgFromUserpic(Integer id);

    /**
     * 上传excel
     * @param fileName
     * @param file
     * @return
     * @throws Exception
     */
    Boolean insertIntoDatebase(String fileName, MultipartFile file) throws  Exception;


    /**
     * 每日考勤数据展示
     * @param num
     * @param day
     * @return
     */
    boolean generateEveryDayMsg(String day, int num) throws IOException;

    /**
     * 周数据导出
     * @param day
     * @param num
     * @return
     */
    boolean generateEveryWeekMsg(String day,int  num) throws IOException, ParseException;

    /**
     * 通过前端传进来的时间 给定周一-周日的时间 用来拼接SQL
     * @param day
     * @return
     * @throws ParseException
     */
    Map<String,String> getWeekStartAndEnd(String day) throws ParseException;


}
