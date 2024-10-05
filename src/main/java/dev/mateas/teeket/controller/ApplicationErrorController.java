package dev.mateas.teeket.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ApplicationErrorController implements ErrorController {
    @RequestMapping("/error")
    public RedirectView handleError(RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView("/");
        redirectAttributes.addAttribute("errorMessage", "Specified page was not found.");
        return redirectView;
    }
}
