package dev.mateas.teeket.controller;

import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.exception.GenericException;
import dev.mateas.teeket.exception.event.EventDoesNotBelongToRequesterException;
import dev.mateas.teeket.exception.event.EventWithSpecifiedIdDoesNotExistException;
import dev.mateas.teeket.exception.ticket.CouldNotGenerateTicketException;
import dev.mateas.teeket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
public class TicketController {
    @Autowired
    private TicketService ticketService;

    private static final String RESOURCES_DIR = Paths.get("src", "main", "resources").toString();

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
            modelAndView.addObject("eventId", eventId);
        } catch (GenericException e) {
            modelAndView.addObject("errorMessage", e.getAdditionalMessage());
        }

        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @RequestMapping(value="events/{eventId}/tickets/generate", method=RequestMethod.POST)
    public RedirectView ticketGeneratePost(Principal principal, @PathVariable String eventId, @RequestParam(value="errorMessage", required = false) String errorMessage, @RequestParam String ticketCount) {
        RedirectView redirectView = null;

        String zipName = null;

        try {
            zipName = ticketService.generateTickets(principal.getName(), eventId, Integer.parseInt(ticketCount));
            redirectView = new RedirectView("/download/" + zipName);
            redirectView.addStaticAttribute("eventId", eventId);
        } catch (GenericException e) {
            redirectView = new RedirectView("/events/" + eventId + "/tickets");
            redirectView.addStaticAttribute("errorMessage", e.getAdditionalMessage());
        }

        return redirectView;
    }

    @RequestMapping(path = "/download/{zipName}", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> downloadGET(RedirectAttributes redirectAttributes, @PathVariable("zipName") String zipName) throws IOException {
        File file = new File(Paths.get(RESOURCES_DIR, "temp", "zip", zipName).toString());

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());

        return new ResponseEntity<>(
                new FileSystemResource(file), respHeaders, HttpStatus.OK
        );
    }
}
