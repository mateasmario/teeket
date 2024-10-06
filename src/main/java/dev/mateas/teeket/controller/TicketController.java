package dev.mateas.teeket.controller;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.exception.GenericException;
import dev.mateas.teeket.service.EventService;
import dev.mateas.teeket.service.TicketService;
import dev.mateas.teeket.type.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
public class TicketController {
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;

    private static final String RESOURCES_DIR = Paths.get("src", "main", "resources").toString();

    @RequestMapping(value = "events/{eventId}/tickets", method = RequestMethod.GET)
    public ModelAndView ticketGet(Principal principal, @PathVariable String eventId, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
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

    @RequestMapping(value = "events/{eventId}/tickets/generate", method = RequestMethod.GET)
    public ModelAndView ticketGenerateGet(Principal principal, RedirectAttributes redirectAttributes, @PathVariable String eventId, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        ModelAndView modelAndView = new ModelAndView("tickets/generate.html");

        try {
            long ticketCount = ticketService.getAvailableTicketCount(principal.getName(), eventId);
            modelAndView.addObject("ticketCount", ticketCount);
            modelAndView.addObject("eventId", eventId);
        } catch (GenericException e) {
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }

        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @RequestMapping(value = "events/{eventId}/tickets/generate", method = RequestMethod.POST)
    public RedirectView ticketGeneratePost(Principal principal, @PathVariable String eventId, @RequestParam(value = "errorMessage", required = false) String errorMessage, @RequestParam String ticketCount) {
        RedirectView redirectView = new RedirectView("/events/" + eventId + "/tickets");

        try {
            ticketService.createTickets(principal.getName(), eventId, Integer.parseInt(ticketCount));
            redirectView.addStaticAttribute("errorMessage", "Tickets have been created successfully.");
        } catch (GenericException e) {
            redirectView.addStaticAttribute("errorMessage", e.getAdditionalMessage());
        }

        return redirectView;
    }

    @RequestMapping(path = "events/{eventId}/tickets/download", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> downloadGET(Principal principal, RedirectAttributes redirectAttributes, @PathVariable String eventId) {
        String zipName = null;

        try {
            zipName = ticketService.generateTickets(principal.getName(), eventId);
        } catch (GenericException e) {
            // ToDo: Add error message
        }

        File file = new File(Paths.get(RESOURCES_DIR, "temp", "zip", zipName).toString());

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());

        return new ResponseEntity<>(
                new FileSystemResource(file), respHeaders, HttpStatus.OK
        );
    }

    @RequestMapping(value = "/events/{eventId}/tickets/delete/{ticketId}", method = RequestMethod.GET)
    public ModelAndView ticketDeleteGet(Principal principal, @PathVariable String eventId, @PathVariable String ticketId) {
        ModelAndView modelAndView;

        try {
            ticketService.deleteTicket(principal.getName(), eventId, ticketId);
            modelAndView = new ModelAndView("redirect:/events/" + eventId + "/tickets");
            modelAndView.addObject("errorMessage", "Ticket has been deleted successfully.");
        } catch (GenericException e) {
            modelAndView = new ModelAndView("events/create.html");
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }

        return modelAndView;
    }

    @RequestMapping(value = "/events/{eventId}/tickets/delete/all", method = RequestMethod.GET)
    public ModelAndView ticketDeleteAllGet(Principal principal, @PathVariable String eventId) {
        ModelAndView modelAndView;

        try {
            ticketService.deleteTickets(principal.getName(), eventId);
            modelAndView = new ModelAndView("redirect:/events/" + eventId + "/tickets");
            modelAndView.addObject("errorMessage", "Tickets have been deleted successfully.");
        } catch (GenericException e) {
            modelAndView = new ModelAndView("events/create.html");
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }

        return modelAndView;
    }

    @RequestMapping(value = "/validate/{eventId}/{ticketId}", method = RequestMethod.GET)
    public ModelAndView validateGet(@PathVariable String eventId, @RequestParam(value = "errorMessage", required = false) String errorMessage, @PathVariable String ticketId) {
        ModelAndView modelAndView;
        try {
            eventService.getEvent(eventId);
            modelAndView = new ModelAndView("validate/password.html");
            modelAndView.addObject("eventId", eventId);
            modelAndView.addObject("ticketId", ticketId);
        } catch (GenericException e) {
            modelAndView = new ModelAndView("redirect:/");
        }

        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/validate/{eventId}/{ticketId}", method = RequestMethod.POST)
    public ModelAndView validatePost(@PathVariable String eventId, @PathVariable String ticketId, @RequestParam("code") String code) {
        ModelAndView modelAndView;

        try {
            Ticket ticket = ticketService.getTicket(eventId, ticketId, code);
            Event event = eventService.getEvent(eventId);
            modelAndView = new ModelAndView("validate/result.html");
            modelAndView.addObject("ticket", ticket);
            modelAndView.addObject("event", event);
            modelAndView.addObject("code", code);
        } catch (GenericException e) {
            modelAndView = new ModelAndView("redirect:/validate/" + eventId + "/" + ticketId);
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/validate/{eventId}/{ticketId}/setstatus/{ticketStatus}", method = RequestMethod.POST)
    public ModelAndView invalidatePost(@PathVariable String eventId, @PathVariable String ticketId, @PathVariable String ticketStatus, @RequestParam("code") String code) {
        ModelAndView modelAndView;

        try {
            ticketService.setTicketStatus(eventId, ticketId, code, TicketStatus.valueOf(ticketStatus));
            Ticket ticket = ticketService.getTicket(eventId, ticketId, code);
            Event event = eventService.getEvent(eventId);
            modelAndView = new ModelAndView("validate/result.html");
            modelAndView.addObject("ticket", ticket);
            modelAndView.addObject("event", event);
            modelAndView.addObject("code", code);
        } catch (GenericException e) {
            modelAndView = new ModelAndView("redirect:/validate/" + eventId + "/" + ticketId);
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }
        return modelAndView;
    }
}
