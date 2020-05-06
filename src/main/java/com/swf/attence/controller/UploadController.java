package com.swf.attence.controller;

import com.swf.attence.service.IUserMsgService;
import com.swf.attence.service.ImageUpload;
import com.swf.attence.service.impl.ImgUploadImpl;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * @author : white.hou
 * @description : 图片上传控制
 * @date: 2019/1/15_18:16
 */
@RestController
public class UploadController {
    @Autowired
    private ImageUpload imageUpload;
    @Autowired
    private IUserMsgService iUserMsgService;

    public static  final  String PATH=System.getProperty("user.dir")+"\\AttenceSystem\\userpic\\";

    public static  final  String USERDATAPATH=System.getProperty("user.dir")+"\\AttenceSystem\\userdata\\";


    /**
     * 图片上传
     * @param multipartFile
     * @param model
     * @return
     */
    @PostMapping("/userMsg/uploadImg")
    @ResponseBody
    public String uploadImsg(@RequestParam(value = "userpic1",required = false) MultipartFile multipartFile, Model model){
        if (imageUpload.imgUpload(multipartFile, PATH)){
            return "true";
        }else {
            return "false";
        }
      }

    /**
     * 文件上传 解析后将数据存入数据库
     * @param multipartFile
     * @return
     */
    @PostMapping("/userMsg/uploadFiles")
      public String uploadFiles(@RequestParam(value = "uploadFiles",required = false) MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        Boolean aBoolean = iUserMsgService.insertIntoDatebase(originalFilename, multipartFile);
        if (aBoolean){
            return "true";
        }else {
            return "false";
        }
    }
    }
