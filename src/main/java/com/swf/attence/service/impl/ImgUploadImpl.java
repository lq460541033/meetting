package com.swf.attence.service.impl;

import com.swf.attence.controller.TimeControController;
import com.swf.attence.service.ImageUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * @author : white.hou
 * @description : 图片上传实现类
 * @date: 2019/1/15_18:12
 */
@Service
public class ImgUploadImpl implements ImageUpload {

    private static final Logger logger = LoggerFactory.getLogger(ImgUploadImpl.class);
    @Override
    public Boolean imgUpload(MultipartFile file,String path) {
        String originalFilename = file.getOriginalFilename();
        if(!file.isEmpty()){
            try{
                Files.copy(file.getInputStream(), Paths.get(path,originalFilename));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public Boolean fileUpload(MultipartFile file,String path) {
        if(!file.isEmpty()){
            try{
                Files.copy(file.getInputStream(), Paths.get(path,file.getName()));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public Boolean download(String fileName) {
        return null;
    }

    @Override
    public Boolean fileDownloadMatch(String path, String fileName) throws IOException {
        Path path1 = Paths.get(path);
        DirectoryStream<Path> list = Files.newDirectoryStream(path1);
        for (Path p:list
                ) {
            if (fileName.equals(p.getFileName())){

            }
        }
        return null;
    }

}
