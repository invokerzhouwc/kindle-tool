package cn.sleighthreesilly.kindletool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :zhouwenchao
 * @Description:
 * @date :2019/12/29
 */
@Controller
public class IndexController {

    @Value("${server.model}")
    private String model;

    @RequestMapping("/index")
    public void index(HttpServletRequest request){
        request.setAttribute("model",model);
    }
    @RequestMapping("/")
    public String base(HttpServletRequest request){
        return "/index";
    }

}
