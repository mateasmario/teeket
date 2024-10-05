package dev.mateas.teeket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView homeGET(@RequestParam(value="errorMessage", required = false) String errorMessage) {
        ModelAndView modelAndView = new ModelAndView("home.html");
        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }
}
