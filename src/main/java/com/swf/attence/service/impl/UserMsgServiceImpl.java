package com.swf.attence.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swf.attence.config.MyException;

import com.swf.attence.entity.DeptMsg;
import com.swf.attence.entity.UserMsg;

import com.swf.attence.mapper.DeptMsgMapper;
import com.swf.attence.mapper.UserMsgMapper;

import com.swf.attence.service.ICameraMsgService;
import com.swf.attence.service.IUserMsgService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.swf.attence.service.ImageUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.swf.attence.controller.UploadController.PATH;
import static javax.swing.text.DefaultStyledDocument.ElementSpec.ContentType;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author auto-genergator
 * @since 2018-12-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMsgServiceImpl extends ServiceImpl<UserMsgMapper, UserMsg> implements IUserMsgService {

    @Autowired
    private UserMsgMapper userMsgMapper;

    @Autowired
    private DeptMsgMapper deptMsgMapper;
    @Autowired
    private ImageUpload imageUpload;
    @Autowired
    private ICameraMsgService iCameraMsgService;

    public static final String ATTENCEDATA = "\\AttenceSystem\\attencedata\\";

    @Override
    public List selectUserMsgAndDeptMsg() {
        List<UserMsg> userMsgs = userMsgMapper.selectUserMsgAndDeptMsg();
        System.out.println("方法selectUserMsgAndDeptMsg查询到的list是： " + userMsgs);
        return userMsgs;
    }

    @Override
    public List selectUserMsgAndDeptMsgById(Integer id) {
        return null;
    }

    @Override
    public List selectUserMsgAndDeptMsgByUserid(String userid) {
        return null;
    }

    @Override
    public void delImgFromUserpic(Integer id) {
        UserMsg userMsg = selectById(id);
        String userid = userMsg.getUserid();
        File file = new File(PATH);
        String realPath = null;
        File[] files = file.listFiles();
        for (File f : files
                ) {
            String name = f.getName();
            if (name.endsWith("jpg")) {
                realPath = PATH + userid + ".jpg";
                File file1 = new File(realPath);
                file1.delete();

            } else if (name.endsWith("png")) {
                realPath = PATH + userid + ".png";
                File file1 = new File(realPath);
                file1.delete();
            } else if (name.endsWith("jpeg")) {
                realPath = PATH + userid + ".jpeg";
                File file1 = new File(realPath);
                file1.delete();

            }
        }
    }

    @Override
    public boolean generateEveryDayMsg(String day, int num) throws IOException {
       /* *//**
         * 工作区
         *//*
        XSSFWorkbook workbook = new XSSFWorkbook();
        *//**
         * sheet工作表
         *//*
        String name=null;
        if(num==1) {
            name="考勤成功";
        }else if (num==2){
            name="迟到";
        }else if (num==3){
            name="早退";
        }else if(num==4){
            name="迟到早退";
        }else if (num==5){
            name="缺勤";
        }
        XSSFSheet sheet = workbook.createSheet("杭州仰天信息科技" + day +name+ "表");
        *//**
         * 表行 0 开始
         *//*
        XSSFRow row = sheet.createRow(0);
        *//**
         * 单元格 0 第一行第一列
         *//*
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("用户工号");
        row.createCell(1).setCellValue("用户姓名");
        row.createCell(2).setCellValue("用户请假状态");
        row.createCell(3).setCellValue("用户进入时间");
        row.createCell(4).setCellValue("用户离开时间");
        if (num==1 || num  ==2 || num==3|| num==4) {
            ArrayList<AttenceMsg> attenceMsgs = attenceMsgMapper.selectByTimeAndState(day + "%", num);
            for (int i = 1; i <= attenceMsgs.size(); i++) {
                XSSFRow sheetRow = sheet.createRow(i);
                *//**
                 * 单元格 0 第一行第一列
                 *//*
                XSSFCell rowCell = sheetRow.createCell(i);
                AttenceMsg a = attenceMsgs.get(i - 1);
                sheetRow.createCell(0).setCellValue(a.getUserid());
                UserMsg userMsg = userMsgMapper.selectUserMsgAndDeptMsgByUserid(a.getUserid());
                if (userMsg==null){
                    sheetRow.createCell(1).setCellValue("未知姓名");
                    sheetRow.createCell(2).setCellValue("未知请假状态");
                    sheetRow.createCell(3).setCellValue("未知进入时间");
                    sheetRow.createCell(4).setCellValue("未知离开时间");
                }else {
                    sheetRow.createCell(1).setCellValue(userMsg.getUsername());
                    if (a.getFailid()==1){
                        sheetRow.createCell(2).setCellValue("已请假");
                    }else {
                        sheetRow.createCell(2).setCellValue("未请假");
                    }
                    sheetRow.createCell(3).setCellValue(a.getCheckInTime());
                    sheetRow.createCell(4).setCellValue(a.getCheckOutTime());
                }
            }
        }else if (num==5){
            List<UserMsg> userMsgs = selectList(new EntityWrapper<UserMsg>().eq("1", 1));
            List<AttenceMsg> attenceMsgs = attenceMsgMapper.selectList(new EntityWrapper<AttenceMsg>().like("check_in_time", day + "%"));
            ArrayList<UserMsg> msgs = new ArrayList<>(16);
            for (AttenceMsg a:attenceMsgs
                 ) {
                UserMsg userMsg = selectOne(new EntityWrapper<UserMsg>().eq("userid", a.getUserid()));
                Iterator<UserMsg> iterator = userMsgs.iterator();
                while (iterator.hasNext()){
                    UserMsg next = iterator.next();
                    if (next.equals(userMsg)){
                        msgs.add(userMsg);
                    }
                }
                userMsgs.removeAll(msgs);
            }
            for (int i = 1; i <= userMsgs.size(); i++) {
                XSSFRow sheetRow = sheet.createRow(i);
                *//**
                 * 单元格 0 第一行第一列
                 *//*
                XSSFCell rowCell = sheetRow.createCell(i);
                UserMsg userMsg = userMsgs.get(i - 1);
                sheetRow.createCell(0).setCellValue(userMsg.getUserid());
                sheetRow.createCell(1).setCellValue(userMsg.getUsername());
            }
        }
        *//**
         * 设置列宽  行高
         *//*
        for (int i=0;i<row.getPhysicalNumberOfCells();i++){
            sheet.setColumnWidth(i,255*20);
        }
        row.setHeightInPoints(30);
        FileOutputStream outputStream = new FileOutputStream(ATTENCEDATA + "杭州仰天信息科技" +day +name+ ".xlsx");
        workbook.write(outputStream);
        outputStream.close();
        System.out.println("已成功生成: "+day+"考勤报表");
        return true;
    }

    @Override
    public boolean generateEveryWeekMsg(String day, int num) throws IOException, ParseException {
        Map<String, String> map = getWeekStartAndEnd(day);
        String start = map.get("start");
        String end = map.get("end");
        *//**
         * 工作区
         *//*
        XSSFWorkbook workbook = new XSSFWorkbook();
        *//**
         * sheet工作表
         *//*
        String name=null;
        if(num==1) {
            name="考勤成功";
        }else if (num==2){
            name="迟到";
        }else if (num==3){
            name="早退";
        }else if(num==4){
            name="迟到早退";
        }else if (num==5){
            name="缺勤";
        }
        XSSFSheet sheet = workbook.createSheet("杭州仰天信息科技" + day +name+ "表");
        *//**
         * 表行 0 开始
         *//*
        XSSFRow row = sheet.createRow(0);
        *//**
         * 单元格 0 第一行第一列
         *//*
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("用户工号");
        row.createCell(1).setCellValue("用户姓名");
        row.createCell(2).setCellValue("用户请假状态");
        row.createCell(3).setCellValue("用户进入时间");
        row.createCell(4).setCellValue("用户离开时间");
        if (num==1 || num  ==2 || num==3|| num==4) {
            List<AttenceMsg> attenceMsgs = iAttenceMsgService.selectList(new EntityWrapper<AttenceMsg>().eq("check_state",num).andNew().between("check_in_time",start,end));
            for (int i = 1; i <= attenceMsgs.size(); i++) {
                XSSFRow sheetRow = sheet.createRow(i);
                *//**
                 * 单元格 0 第一行第一列
                 *//*
                XSSFCell rowCell = sheetRow.createCell(i);
                AttenceMsg a = attenceMsgs.get(i - 1);
                sheetRow.createCell(0).setCellValue(a.getUserid());
                UserMsg userMsg = userMsgMapper.selectUserMsgAndDeptMsgByUserid(a.getUserid());
                if (userMsg==null){
                    sheetRow.createCell(1).setCellValue("未知姓名");
                    sheetRow.createCell(2).setCellValue("未知请假状态");
                    sheetRow.createCell(3).setCellValue("未知进入时间");
                    sheetRow.createCell(4).setCellValue("未知离开时间");
                }else {
                    sheetRow.createCell(1).setCellValue(userMsg.getUsername());
                    if (a.getFailid()==1){
                        sheetRow.createCell(2).setCellValue("已请假");
                    }else {
                        sheetRow.createCell(2).setCellValue("未请假");
                    }
                    sheetRow.createCell(3).setCellValue(a.getCheckInTime());
                    sheetRow.createCell(4).setCellValue(a.getCheckOutTime());
                }
            }
        }else if (num==5){
            List<UserMsg> userMsgs = selectList(new EntityWrapper<UserMsg>().eq("1", 1));
            List<AttenceMsg> attenceMsgs = iAttenceMsgService.selectList(new EntityWrapper<AttenceMsg>().eq("check_state",num).andNew().between("check_in_time",start,end));
            ArrayList<UserMsg> msgs = new ArrayList<>(16);
            for (AttenceMsg a:attenceMsgs
                    ) {
                UserMsg userMsg = selectOne(new EntityWrapper<UserMsg>().eq("userid", a.getUserid()));
                Iterator<UserMsg> iterator = userMsgs.iterator();
                while (iterator.hasNext()){
                    UserMsg next = iterator.next();
                    if (next.equals(userMsg)){
                        msgs.add(userMsg);
                    }
                }
                userMsgs.removeAll(msgs);
            }
            for (int i = 1; i <= userMsgs.size(); i++) {
                XSSFRow sheetRow = sheet.createRow(i);
                *//**
                 * 单元格 0 第一行第一列
                 *//*
                XSSFCell rowCell = sheetRow.createCell(i);
                UserMsg userMsg = userMsgs.get(i - 1);
                sheetRow.createCell(0).setCellValue(userMsg.getUserid());
                sheetRow.createCell(1).setCellValue(userMsg.getUsername());
            }
        }
        *//**
         * 设置列宽  行高
         *//*
        for (int i=0;i<row.getPhysicalNumberOfCells();i++){
            sheet.setColumnWidth(i,255*20);
        }
        row.setHeightInPoints(30);
        FileOutputStream outputStream = new FileOutputStream(ATTENCEDATA + "杭州仰天信息科技" +start+"-----"+end + ".xlsx");
        workbook.write(outputStream);
        outputStream.close();
        System.out.println("已成功生成: "+start+"-----"+end+"考勤报表");
        return true;*/
        return true;
    }

    @Override
    public boolean generateEveryWeekMsg(String day, int num) throws IOException, ParseException {
        return false;
    }

    @Override
    public Boolean insertIntoDatebase(String fileName, MultipartFile file) throws Exception {
        boolean notNull = false;
        List<UserMsg> userMsgs = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            if (row.getCell(1).getCellType() != 1) {
                throw new MyException("导入失败(第" + (i + 1) + "行,姓名请设为文本格式)");
            }
            /**
             * 用户名
             */
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String userid = row.getCell(0).getStringCellValue();
            UserMsg userMsg = userMsgMapper.selectUserMsgAndDeptMsgByUserid(userid);
            if (userid == null || userid.isEmpty()) {
                throw new MyException("导入失败（第" + (i + 1) + "行，用户工号未填写)");
            } else if (userMsg!=null) {
                throw new MyException("导入失败（第" + (i + 1) + "行，该用户已存在)");
            }
            /**
             * 用户姓名
             */
            String username = row.getCell(1).getStringCellValue();
            if (username == null || username.isEmpty()) {
                throw new MyException("导入失败（第" + (i + 1) + "行，用户姓名未填写)");
            }
            /**
             * 性别
             */
            String gender = row.getCell(2).getStringCellValue();
            Integer realGender;
            if (gender == null || gender.isEmpty()) {
                throw new MyException("导入失败（第" + (i + 1) + "行，用户性别未填写)");
            } else {
                String gender1 = "男";
                String gender0 = "女";
                if (gender1.equals(gender)) {
                    realGender = 1;
                } else if (gender0.equals(gender)) {
                    realGender = 0;
                } else {
                    throw new MyException("导入失败（第" + (i + 1) + "行，用户性别只能为男/女)");
                }
            }
            /**
             * 部门
             */
            String deptid = row.getCell(3).getStringCellValue();
            String realDeptid;
            if (deptid == null || deptid.isEmpty()) {
                throw new MyException("导入失败（第" + (i + 1) + "行，用户部门未填写)");
            } else {
                List<DeptMsg> deptname = deptMsgMapper.selectList(new EntityWrapper<DeptMsg>().eq("deptname", deptid));
                DeptMsg deptMsg = deptname.get(0);
                realDeptid = deptMsg.getDeptid();
            }
            /**
             * 图片路径
             */
            String userpic = row.getCell(4).getStringCellValue();
            if (userpic == null || userpic.isEmpty()) {
                throw new MyException("导入失败（第" + (i + 1) + "行，用户照片路径未填写)");
            } else {
                if (!userpic.matches("^.+\\.(?i)(jpg)$") && !userpic.matches("^.+\\.(?i)(jpeg)$") && !userpic.matches("^.+\\.(?i)(png)$")) {
                    throw new MyException("导入失败（第" + (i + 1) + "行，用户照片路径填写错误，格式应为jpg、jpeg、png)");
                } else if (!userpic.contains(userid)) {
                    throw new MyException("导入失败（第" + (i + 1) + "行，用户照片路径填写错误,照片名应和工号一致)");
                }
            }
            UserMsg userMsg1 = new UserMsg();
            userMsg1.setUserid(userid);
            userMsg1.setUsername(username);
            userMsg1.setDeptid(realDeptid);
            userMsg1.setGender(realGender);
            try {
                File file1 = new File(userpic);
                FileInputStream fileInputStream = new FileInputStream(file1);
                String name = file1.getName();
                MockMultipartFile mockMultipartFile = new MockMultipartFile(name,fileInputStream);
                if (imageUpload.fileUpload(mockMultipartFile, PATH)) {
                    userMsg1.setUserpic(name);
                    userMsgs.add(userMsg1);
                    insert(userMsg1);
                    /**
                     * 添加到设备
                     */
                    iCameraMsgService.uploadUserPicAndUserMessageByOne(userMsg1);
                    notNull=true;
                } else {
                    throw new MyException("（第" + (i + 1) + "行用户照片导入失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notNull;
    }


    @Override
    public synchronized Map<String, String> getWeekStartAndEnd(String day) throws ParseException {
        HashMap<String, String> map = new HashMap<>(16);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse(day);
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        instance.add(Calendar.DAY_OF_YEAR,4);
        Date time = instance.getTime();
        String weekEnd = simpleDateFormat.format(time);
        map.put("end",weekEnd);
        instance.add(Calendar.DAY_OF_YEAR,-6);
        Date time1 = instance.getTime();
        String weekStart = simpleDateFormat.format(time1);
        map.put("start",weekStart);
        return map;
    }

    public static void main(String[] args) throws ParseException {
        String day="2019-03-13";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse(day);
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        instance.add(Calendar.DAY_OF_YEAR,4);
        Date time = instance.getTime();
        String format = simpleDateFormat.format(time);
        System.out.println(format);
        instance.add(Calendar.DAY_OF_YEAR,-6);
        Date time1 = instance.getTime();
        String format1 = simpleDateFormat.format(time1);
        System.out.println(format1);
    }

}
