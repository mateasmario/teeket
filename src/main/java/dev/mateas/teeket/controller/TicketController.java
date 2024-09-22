package dev.mateas.teeket.controller;

import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.exception.GenericException;
import dev.mateas.teeket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@Controller
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @RequestMapping(value = "events/{eventId}/tickets", method = RequestMethod.GET)
    public ModelAndView ticketGet(Principal principal, @PathVariable String eventId, @RequestParam(value="errorMessage", required = false) String errorMessage) {
        ModelAndView modelAndView = new ModelAndView("tickets/list.html");

        List<Ticket> ticketList = null;
        try {
            ticketList = ticketService.getTickets(principal.getName(), eventId);
        } catch (GenericException e) {
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }
        modelAndView.addObject("ticketList", ticketList);

        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @RequestMapping(value="events/{eventId}/tickets/{id}/delete", method=RequestMethod.GET)
    public RedirectView ticketDelete(Principal principal, RedirectAttributes redirectAttributes, @PathVariable String eventId, @PathVariable String ticketId) {
        RedirectView redirectView = new RedirectView("/events/" + eventId + "/tickets", true);

        try {
            ticketService.deleteTicket(principal.getName(), ticketId);
            redirectAttributes.addAttribute("errorMessage", "Ticket has been deleted successfully.");
        } catch (GenericException e) {
            redirectAttributes.addAttribute("errorMessage", e.getAdditionalMessage());
        }

        return redirectView;
    }

    @RequestMapping(value="events/{eventId}/tickets/generate", method=RequestMethod.GET)
    public ModelAndView ticketGenerateGet(Principal principal, RedirectAttributes redirectAttributes, @PathVariable String eventId, @RequestParam(value="errorMessage", required = false) String errorMessage) {
        ModelAndView modelAndView = new ModelAndView("tickets/generate.html");

        try {
            long ticketCount = ticketService.getAvailableTicketCount(principal.getName(), eventId);
            modelAndView.addObject("ticketCount", ticketCount);
        } catch (GenericException e) {
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }

        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }}
