package dev.mateas.teeket.controller.authentication;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.service.authentication.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public ModelAndView eventGet(Principal principal) {
        List<Event> eventList = eventService.getEvents(principal.getName());
        ModelAndView modelAndView = new ModelAndView("events.html");
        modelAndView.addObject("eventList", eventList);
        return modelAndView;
    }
}
