package dev.mateas.teeket.controller.authentication;

import dev.mateas.teeket.dto.authentication.RegisterDto;
import dev.mateas.teeket.exception.GenericException;
import dev.mateas.teeket.service.authentication.RegisterService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerGet() {
        return new ModelAndView("authentication/register.html");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerPost(HttpServletRequest httpServletRequest, @ModelAttribute RegisterDto registerDto) {
        ModelAndView modelAndView;

        try {
            registerService.register(registerDto);
            httpServletRequest.login(registerDto.getUsername(), registerDto.getPassword());
            modelAndView = new ModelAndView("redirect:/");

        } catch (GenericException e) {
            modelAndView = new ModelAndView("authentication/register.html");
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        } catch (ServletException e) {
            modelAndView = new ModelAndView("authentication/register.html");
            modelAndView.addObject("errorMessage", "There was a servlet error while trying to create an account for you.");
        }

        return modelAndView;
    }
}
