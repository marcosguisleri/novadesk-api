package br.dev.guisleri.novadesk.controller;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setOpenDate(LocalDateTime.now());

        ticketService.createTicket(ticket);

        return ticket;
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(
            @PathVariable long id,
            @RequestBody Ticket ticket
    ) {
        return ticketService.updateTicket(id, ticket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicketById(@PathVariable long id) {

        boolean deleted = ticketService.deleteTicketById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Ticket removido com sucesso.");
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Optional<Ticket> getTicketById(@PathVariable long id) {
        return ticketService.getTicketById(id);
    }

}
