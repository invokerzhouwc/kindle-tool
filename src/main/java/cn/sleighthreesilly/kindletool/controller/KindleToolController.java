package cn.sleighthreesilly.kindletool.controller;

import cn.sleighthreesilly.kindletool.bean.EmailMessageDO;
import cn.sleighthreesilly.kindletool.service.SendEmailService;
import cn.sleighthreesilly.kindletool.worker.SendEmailWorker;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
@RestController
public class KindleToolController {

    @Autowired
    private SendEmailWorker sendEmailWorker;

    private static long FILE_LENGTH_QQ  = 50*1024*1024;

    private static long FILE_LENGTH_163 = 20*1024*1024;

    @PostMapping("/pull")
    public String pull(@RequestParam("files") List<MultipartFile> files,
                       @RequestParam("kindleEmail") String kindleEmail,
                       @RequestParam("sendEmail") String sendEmail,
                       @RequestParam("password") String password){
        List<File> targetFiles = new ArrayList<>();
        for(MultipartFile file : files){
            File targetFile = new File(file.getOriginalFilename());
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long length = targetFile.length();
            if(sendEmail.toLowerCase().endsWith("@qq.com")&&length>FILE_LENGTH_QQ){
                return "文件过大";
            }
            targetFiles.add(targetFile);
        }

        EmailMessageDO emailMessageDO = new EmailMessageDO(targetFiles, kindleEmail, sendEmail, password);
        boolean put = sendEmailWorker.put(emailMessageDO);
        if(put){
            sendEmailWorker.waitSend(emailMessageDO);
            return "推送中";
        }
        return "推送失败";
    }

    @PostMapping("/status")
    public Map<String,String> status(){
        Map<String, String> stringStringMap = sendEmailWorker.allStatus();
        if(stringStringMap.size()==0){
            return null;
        }
        return sendEmailWorker.allStatus();
    }
}
