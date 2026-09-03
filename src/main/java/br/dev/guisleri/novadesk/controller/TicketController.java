package br.dev.guisleri.novadesk.controller;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.TicketResponseDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TicketResponseDTO> createTicket(
            @RequestBody CreateTicketRequestDTO requestDTO
    ) {
        Ticket ticket = ticketService.createTicket(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToResponseDTO(ticket));
    }

    @PutMapping("/{id}")
    public TicketResponseDTO updateTicket(
            @PathVariable long id,
            @RequestBody UpdateTicketRequestDTO requestDTO
    ) {
        Ticket updateTicket = ticketService.updateTicket(id, requestDTO);

        return convertToResponseDTO(updateTicket);
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
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.getAllTickets()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public Optional<TicketResponseDTO> getTicketById(@PathVariable long id) {
        return ticketService.getTicketById(id)
                .map(this::convertToResponseDTO);
    }

    private TicketResponseDTO convertToResponseDTO(Ticket ticket) {
        return new TicketResponseDTO(ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getRequester(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getOpenDate());
    }

}
