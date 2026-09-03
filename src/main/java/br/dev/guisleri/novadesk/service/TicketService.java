package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Criação, edição e delete
    public void createTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public Ticket updateTicket(long id, Ticket updatedTicket) {
        return ticketRepository.update(id, updatedTicket);
    }

    public boolean deleteTicketById(long id) {
        return ticketRepository.deleteById(id);
    }

    // Buscas
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(long id) {
        return ticketRepository.findById(id);
    }

}
