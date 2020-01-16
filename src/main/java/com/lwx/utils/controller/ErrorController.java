package com.lwx.utils.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author luwenxin
 * @create 2020/1/16
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    /**
     * @author: LWX
     * @date: 2020/1/16 13:54
     * 404页面
     */
    @RequestMapping("/404")
    public String error404() {
        return "/status/404page.html  ";
    }

    /**
     * @author: LWX
     * @date: 2020/1/16 13:54
     * 500页面
     */
    public String error500() {
        return "";
    }
}
