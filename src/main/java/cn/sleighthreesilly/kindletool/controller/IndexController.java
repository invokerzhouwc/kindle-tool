package cn.sleighthreesilly.kindletool.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
@Controller
public class IndexController {

    @Value("${server.model:db}")
    private String model;

    @Value("${storage.message:false}")
    private boolean storageMessage;
    @Value("${storage.message.path:null}")
    private String storageMessagePath;

    @RequestMapping("/index")
    public void index(HttpServletRequest request){
        request.setAttribute("model",model);
    }
    @RequestMapping("/")
    public String base(HttpServletRequest request){
        return "/index";
    }

    @RequestMapping("/getInfo")
    @ResponseBody
    public JSONObject getInfo() throws IOException {
        if(storageMessage&& !StringUtils.isEmpty(storageMessagePath)){
            File file = new File(storageMessagePath+"/info");
            if(!file.exists()){
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            return JSONObject.parseObject(stringBuffer.toString());
        }
        return null;
    }



}
