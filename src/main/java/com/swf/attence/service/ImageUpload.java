package com.swf.attence.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : white.hou
 * @description : 图片上传接口
 * @date: 2019/1/15_18:10
 */
public interface ImageUpload {
    /**
     * 上传图片
     * @param file
     * @param path
     * @return
     */
    Boolean imgUpload(MultipartFile file,String path);

    /**
     * 批量上传 图片上传
     * @param file
     * @param path
     * @return
     */
    Boolean fileUpload(MultipartFile file,String path);

    /**
     * 从指定目录中匹配文件并下载
     * @param path
     * @param fileName
     * @return
     */
    Boolean fileDownloadMatch(String path,String fileName) throws IOException;

    /**
     * 文件下载办法
     * @param fileName
     * @return
     */
    Boolean download(String fileName);
}
