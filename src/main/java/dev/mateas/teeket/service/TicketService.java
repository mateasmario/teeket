package dev.mateas.teeket.service;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.exception.event.EventDoesNotBelongToRequesterException;
import dev.mateas.teeket.exception.event.EventWithSpecifiedIdDoesNotExistException;
import dev.mateas.teeket.exception.ticket.*;
import dev.mateas.teeket.repository.EventRepository;
import dev.mateas.teeket.repository.TicketRepository;
import dev.mateas.teeket.type.TicketStatus;
import dev.mateas.teeket.util.*;
import dev.mateas.teeket.util.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StringGenerator stringGenerator;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;
    @Autowired
    private ImageGenerator imageGenerator;
    @Autowired
    private ZipFileGenerator zipFileGenerator;

    public List<Ticket> getTickets(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        Event event = eventOptional.get();
        if (!event.getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        return ticketRepository.findByEvent(eventId);
    }

    public long getAvailableTicketCount(String username, String eventId) throws TicketWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException, EventWithSpecifiedIdDoesNotExistException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        Event event = eventOptional.get();
        long currentCount = ticketRepository.countByEvent(event.getId());

        return switch (event.getEventType()) {
            case STANDARD -> 50 - currentCount;
            case EXTENDED -> 100 - currentCount;
            case OVERCROWDED -> 250 - currentCount;
        };
    }

    public void createTickets(String username, String eventId, int ticketCount) throws EventDoesNotBelongToRequesterException, EventWithSpecifiedIdDoesNotExistException, CouldNotGenerateTicketException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        int currentTicketCount = 0;
        while(currentTicketCount < ticketCount) {
            try {
                Ticket ticket = generateCode(eventId);
                ticketRepository.insert(ticket);
            } catch (IOException e) {
                throw new CouldNotGenerateTicketException();
            }
            currentTicketCount++;
        }
    }

    private Ticket generateCode(String eventId) throws IOException {
        String ticketId;
        boolean exists;

        do {
            ticketId = stringGenerator.generateString(StringType.ALPHANUMERIC,10);
            exists = ticketRepository.findById(ticketId).isPresent();
        } while(exists);

        return new Ticket(ticketId, LocalDateTime.now(), TicketStatus.VALID, eventId);
    }

    public String generateTickets(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException, CouldNotGenerateZipFileException, CouldNotGenerateTicketException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        Event event = eventOptional.get();
        List<Ticket> ticketList = ticketRepository.findByEvent(event.getId());

        for(Ticket ticket : ticketList) {
            try {
                qrCodeGenerator.generateQRCode(eventId, ticket.getCode());
            } catch (IOException e) {
                throw new CouldNotGenerateTicketException();
            }
        }

        for(Ticket ticket : ticketList) {
            try {
                imageGenerator.generateTicketImages(event, ticket);
            } catch (IOException e) {
                throw new CouldNotGenerateTicketException();
            }
        }

        String zipName;

        try {
            zipName = zipFileGenerator.generateZipFile(ticketList);
        } catch (IOException e) {
            throw new CouldNotGenerateZipFileException();
        }

        for(Ticket ticket : ticketList) {
            File barcode = new File(FileManagementUtils.getBarcodeName(ticket));
            File ticketImage = new File(FileManagementUtils.getTicketName(ticket));

            if (barcode.exists()) {
                if (!barcode.delete()) {
                    throw new CouldNotGenerateZipFileException();
                }
            }

            if (ticketImage.exists()) {
                if (!ticketImage.delete()) {
                    throw new CouldNotGenerateZipFileException();
                }
            }
        }

        return zipName;
    }

    public void deleteTicket(String username, String eventId, String ticketId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException, TicketWithSpecifiedIdDoesNotExistException, TicketDoesNotBelongToSpecifiedEventIdException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (ticketOptional.isEmpty()) {
            throw new TicketWithSpecifiedIdDoesNotExistException();
        }

        Ticket ticket = ticketOptional.get();

        if (!ticket.getEvent().equals(eventId)) {
            throw new TicketDoesNotBelongToSpecifiedEventIdException();
        }

        ticketRepository.delete(ticket);
    }

    public void deleteTickets(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException, TicketWithSpecifiedIdDoesNotExistException, TicketDoesNotBelongToSpecifiedEventIdException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        ticketRepository.deleteByEvent(eventId);
    }

    public Ticket getTicket(String eventId, String ticketId, String code) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException, TicketWithSpecifiedIdDoesNotExistException, TicketDoesNotBelongToSpecifiedEventIdException, EventCodeIsInvalidException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getModerationCode().equals(code)) {
            throw new EventCodeIsInvalidException();
        }

        Optional<Ticket> ticketOptional = ticketRepository.findByCode(ticketId);

        if (ticketOptional.isEmpty()) {
            throw new TicketWithSpecifiedIdDoesNotExistException();
        }

        return ticketOptional.get();
    }

    public void setTicketStatus(String eventId, String ticketId, String code, TicketStatus ticketStatus) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException, TicketWithSpecifiedIdDoesNotExistException, TicketDoesNotBelongToSpecifiedEventIdException, EventCodeIsInvalidException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getModerationCode().equals(code)) {
            throw new EventCodeIsInvalidException();
        }

        Optional<Ticket> ticketOptional = ticketRepository.findByCode(ticketId);

        if (ticketOptional.isEmpty()) {
            throw new TicketWithSpecifiedIdDoesNotExistException();
        }

        Ticket ticket = ticketOptional.get();
        ticket.setStatus(ticketStatus);
        ticketRepository.save(ticket);
    }
}
