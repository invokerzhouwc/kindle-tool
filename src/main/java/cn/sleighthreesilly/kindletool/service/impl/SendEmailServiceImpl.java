package cn.sleighthreesilly.kindletool.service.impl;

import cn.sleighthreesilly.kindletool.bean.EmailMessageDO;
import cn.sleighthreesilly.kindletool.service.SendEmailService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
@Service("sendEmailService")
public class SendEmailServiceImpl implements SendEmailService {

    private Logger logger = LoggerFactory.getLogger(SendEmailServiceImpl.class);

    @Value("${spring.mail.host.qq}")
    private String hostQq;

    @Value("${spring.mail.host.163}")
    private String host163;

    @Value("${spring.mail.port:0}")
    private int port;

    @Override
    public Boolean send(EmailMessageDO emailMessageDO,File file) {
        String sendEmail = emailMessageDO.getSendEmail();
        String password = emailMessageDO.getPassword();
        String kindleEmail = emailMessageDO.getKindleEmail();
        JavaMailSenderImpl javaMailSender = buildJavaMailSender(sendEmail, password);
        MimeMessage mimeMailMessage = null;
        boolean isSendSuccess = false;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sendEmail);
            mimeMessageHelper.setTo(kindleEmail);
            String originalFilename = file.getName();
            mimeMessageHelper.setSubject(originalFilename);
            mimeMessageHelper.setText(originalFilename);
            //文件路径

            FileSystemResource annexFile = new FileSystemResource(file);
            mimeMessageHelper.addAttachment(originalFilename, annexFile);

            javaMailSender.send(mimeMailMessage);
            isSendSuccess = true;
        } catch (Exception e) {
            logger.error("邮件发送失败,错误信息:{}", e.getMessage());
            isSendSuccess = false;
        }finally {
            if(file!=null){
                file.delete();
            }
        }
        return isSendSuccess;
    }

    public JavaMailSenderImpl buildJavaMailSender(String sendEmail,String password){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        if(sendEmail.endsWith("@qq.com")){
            javaMailSender.setHost(hostQq);
        }else if(sendEmail.endsWith("@163.com")){
            javaMailSender.setHost(host163);
        }
        if(port != 0){
            javaMailSender.setPort(port);
        }
        javaMailSender.setUsername(sendEmail);
        javaMailSender.setPassword(password);
        javaMailSender.setDefaultEncoding("Utf-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}
