package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.exception.TicketNotFoundException;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Criação, edição e delete
    public Ticket createTicket(CreateTicketRequestDTO requestDTO) {
        Ticket ticket = new Ticket();

        ticket.setTitle(requestDTO.title());
        ticket.setDescription(requestDTO.description());
        ticket.setRequester(requestDTO.requester());
        ticket.setPriority(requestDTO.priority());

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setOpenDate(LocalDateTime.now());

        ticketRepository.save(ticket);

        return ticket;
    }

    public Ticket updateTicket(long id, UpdateTicketRequestDTO requestDTO) {
        Ticket updatedTicket = new Ticket();

        updatedTicket.setTitle(requestDTO.title());
        updatedTicket.setDescription(requestDTO.description());
        updatedTicket.setRequester(requestDTO.requester());
        updatedTicket.setPriority(requestDTO.priority());
        updatedTicket.setStatus(requestDTO.status());

        Ticket updatedTicketResult = ticketRepository.update(id, updatedTicket);

        if (updatedTicketResult == null) {
            throw new TicketNotFoundException(id);
        }

        return updatedTicketResult;
    }

    public void deleteTicketById(long id) {
        boolean deleted = ticketRepository.deleteById(id);

        if (!deleted) {
            throw new TicketNotFoundException(id);
        }

    }

    // Buscas
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

}
