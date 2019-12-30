package cn.sleighthreesilly.kindletool.service;

import cn.sleighthreesilly.kindletool.bean.EmailMessageDO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
public interface SendEmailService {

    Boolean send(EmailMessageDO emailMessageDO, File file);
}
