package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {


    @Reference
    private HouseImageService houseImageService;


    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String goUploadPage(@PathVariable("houseId")Long houseId,@PathVariable("type")Integer type,Map map)
    {
        //将房源Id和图片的类型放入request域中
        map.put("houseId",houseId);
        map.put("type",type);
        return "house/upload";
    }
    //上传图片
    @ResponseBody
    @RequestMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId")Long houseId, @PathVariable("type")Integer type,
                         @RequestParam("file")MultipartFile[] files){
        try {
        if(files!=null && files.length>0)
        {
            for (MultipartFile file : files) {
               //获取字节数组
                    byte[] bytes = file.getBytes();
                    //获取文件的名字
                String originalFilename = file.getOriginalFilename();
                //通过UUId随机生成一个字符串作为图片的名字
                String newFileName = UUID.randomUUID().toString();
                //调用qiniuutil工具类上传
                QiniuUtil.upload2Qiniu(bytes,newFileName);
                //创建对象
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setType(type);
                houseImage.setImageName(originalFilename);
                //设置图片的路径,路径的格式：http://七牛云的域名/图片名字
                houseImage.setImageUrl("http://rtmav1b4e.hn-bkt.clouddn.com/"+newFileName);
                //调用添加的方法
                houseImageService.insert(houseImage);
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取字节数组
        return Result.ok();
    }
    //删除房源或房产图片
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id)
    {
        //调用删除的方法
        houseImageService.delete(id);
        //重定向详情页面
        return "redirect:/house/"+houseId;
    }
}
