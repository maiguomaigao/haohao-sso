package xyz.haohao.samples.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/nonfilter2")
@Log4j2
public class TestNonfilter2Controller {

    @GetMapping("/a")
    @ResponseBody
    public String a(HttpServletRequest request, HttpServletResponse response) {
        return "welcome to: nonfilter2 a ";
    }

    @GetMapping("/b")
    @ResponseBody
    public String b(HttpServletRequest request, HttpServletResponse response) {
        return "welcome to: nonfilter2 b";
    }

}