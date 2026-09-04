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
        Ticket ticketToUpdate = getTicketById(id);

        ticketToUpdate.setTitle(requestDTO.title());
        ticketToUpdate.setDescription(requestDTO.description());
        ticketToUpdate.setRequester(requestDTO.requester());
        ticketToUpdate.setStatus(requestDTO.status());
        ticketToUpdate.setPriority(requestDTO.priority());

        return ticketRepository.save(ticketToUpdate);
    }

    public void deleteTicketById(long id) {
        Ticket ticketToDelete = getTicketById(id);
        ticketRepository.delete(ticketToDelete);
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
