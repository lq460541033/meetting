package com.swf.attence.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swf.attence.entity.CameraMsg;
import com.swf.attence.hikConfig.ClientDemo;
import com.swf.attence.service.ICameraMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author : white.hou
 * @description : 设备管理
 * @date: 2018/12/31_16:18
 */
@Controller
public class CameraController {
    @Autowired
    private ICameraMsgService iCameraMsgService;

    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

    /**
     * 分页列出所有设备
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/cameras",method =GET)
    public String userItemPages(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<CameraMsg> cameraMsgs = iCameraMsgService.selectList(new EntityWrapper<CameraMsg>().eq("1", 1));
        System.out.println(cameraMsgs);
        PageInfo<CameraMsg> cameraMsgPageInfo = new PageInfo<>(cameraMsgs, 5);
        model.addAttribute("cameraMsgPageInfo",cameraMsgPageInfo);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("isFirstPage",cameraMsgPageInfo.isIsFirstPage());
        model.addAttribute("totalPages",cameraMsgPageInfo.getPages());
        model.addAttribute("isLastPage",cameraMsgPageInfo.isIsLastPage());
        return "camera/camera_list";
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @GetMapping("/camera")
    public String toInsertCamera(){
        return "camera/camera_add";
    }

    /**
     * 添加设备
     * @param cameraMsg
     * @return
     */
    @PostMapping("/camera")
    public String insertCamera(CameraMsg cameraMsg,Model model){
        boolean b = iCameraMsgService.cameraidExist(cameraMsg);
        System.out.println(b);
        try{
            if (b){
                iCameraMsgService.insert(cameraMsg);
                model.addAttribute("msg","添加成功");
                logger.info(cameraMsg+"添加成功");
            }else {
                model.addAttribute("msg","对不起，该设备号已存在，请检查您的输入");
                logger.info(cameraMsg+" 设备已存在");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/cameras";
    }

    /**
     *跳转到编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/camera/{id}")
    public String toUpdateCamera(@PathVariable("id") Integer id, Model model){
        CameraMsg cameraMsg = iCameraMsgService.selectById(id);
        model.addAttribute("camera",cameraMsg);
        return "camera/camera_add";
    }

    /**
     * 修改用户信息
     * @param cameraMsg
     * @return
     */
    @PutMapping("/camera")
    public String updateCamera(CameraMsg cameraMsg){
        boolean b = iCameraMsgService.cameraidExist(cameraMsg);
        System.out.println(b);
        try{
            iCameraMsgService.updateById(cameraMsg);
            logger.info(cameraMsg+"修改成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/cameras";
    }

    /**
     * 删除设备
     * @param id
     * @return
     */
    @DeleteMapping("/camera/{id}")
    public String deleteCamera(@PathVariable("id") Integer id){
        try {
            iCameraMsgService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/cameras";
    }


}
