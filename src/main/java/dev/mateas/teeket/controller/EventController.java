package dev.mateas.teeket.controller;

import dev.mateas.teeket.dto.EventDto;
import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.exception.GenericException;
import dev.mateas.teeket.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public ModelAndView eventGet(Principal principal, @RequestParam(value="errorMessage", required = false) String errorMessage) {
        List<Event> eventList = eventService.getEvents(principal.getName());
        ModelAndView modelAndView = new ModelAndView("events/list.html");
        modelAndView.addObject("eventList", eventList);

        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/events/create", method = RequestMethod.GET)
    public ModelAndView eventCreateGet() {
        return new ModelAndView("events/create.html");
    }

    @RequestMapping(value = "/events/create", method = RequestMethod.POST)
    public ModelAndView eventCreatePost(Principal principal, @ModelAttribute EventDto eventDto) {
        ModelAndView modelAndView;

        try {
            eventService.createEvent(principal.getName(), eventDto);
            modelAndView = new ModelAndView("redirect:/events");
        } catch (GenericException e) {
            modelAndView = new ModelAndView("events/create.html");
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }

        return modelAndView;
    }

    @RequestMapping(value="events/{id}/delete", method=RequestMethod.GET)
    public RedirectView eventDelete(Principal principal, RedirectAttributes redirectAttributes, @PathVariable String id) {
        RedirectView redirectView = new RedirectView("/events", true);

        try {
            eventService.deleteEvent(principal.getName(), id);
            redirectAttributes.addAttribute("errorMessage", "Event has been deleted successfully.");
        } catch (GenericException e) {
            redirectAttributes.addAttribute("errorMessage", e.getAdditionalMessage());
        }

        return redirectView;
    }

    @RequestMapping(value="events/{id}/regenerate", method=RequestMethod.GET)
    public RedirectView regenerate(Principal principal, RedirectAttributes redirectAttributes, @PathVariable String id) {
        RedirectView redirectView = new RedirectView("/events", true);

        try {
            eventService.regenerateModerationCode(principal.getName(), id);
            redirectAttributes.addAttribute("errorMessage", "Code has been successfully regenerated.");
        } catch (GenericException e) {
            redirectAttributes.addAttribute("errorMessage", e.getAdditionalMessage());
        }

        return redirectView;
    }
}
