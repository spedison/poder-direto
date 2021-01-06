package com.spedison.poderdireto.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping(path= {"/index","/"})
    public ModelAndView index(){
        return new ModelAndView("index");
    }

}
