package cn.sleighthreesilly.kindletool.bean;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
public class EmailMessageDO {

    private List<File> files;
    private String kindleEmail;
    private String sendEmail;
    private String password;

    public EmailMessageDO(List<File> files, String kindleEmail, String sendEmail, String password) {
        this.files = files;
        this.kindleEmail = kindleEmail;
        this.sendEmail = sendEmail;
        this.password = password;
    }

    public List<File> getFile() {
        return files;
    }

    public void setFile(List<File> files) {
        this.files = files;
    }

    public String getKindleEmail() {
        return kindleEmail;
    }

    public void setKindleEmail(String kindleEmail) {
        this.kindleEmail = kindleEmail;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "EmailMessageDO{" +
                ", kindleEmail='" + kindleEmail + '\'' +
                ", sendEmail='" + sendEmail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
