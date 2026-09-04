package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.exception.TicketNotFoundException;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void shouldReturnTicketWhenIdExists() {

        Ticket ticketExample = ticketExample1();

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticketExample));

        Ticket ticketById = ticketService.getTicketById(1L);

        assertEquals(1L, ticketById.getId());
        assertEquals("Teste 1", ticketById.getTitle());

    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {

        when(ticketRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.getTicketById(999L)
        );

    }

    @Test
    void shouldCreateTicket() {

        CreateTicketRequestDTO createTicketRequestDTO = createTicketRequestDTOExample();

        Ticket ticket = ticketService.createTicket(createTicketRequestDTO);

        assertEquals("Teste", ticket.getTitle());
        assertEquals("Descrição teste", ticket.getDescription());
        assertEquals("Marcos", ticket.getRequester());
        assertEquals(TicketPriority.CRITICAL, ticket.getPriority());

        assertEquals(TicketStatus.OPEN, ticket.getStatus());
        assertNotNull(ticket.getOpenDate());

        verify(ticketRepository).save(ticket);

    }

    @Test
    void shouldUpdateTicketWhenIdExists() {

        UpdateTicketRequestDTO requestDTO = updateTicketRequestDTOExample();

        Ticket ticketExample = ticketExample1();
        LocalDateTime originalOpenDate = ticketExample.getOpenDate();

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticketExample));

        ticketService.updateTicket(1L, requestDTO);

        assertEquals("Teste atualizado", ticketExample.getTitle());
        assertEquals("Descrição atualizada", ticketExample.getDescription());
        assertEquals("Marcos Atualizado", ticketExample.getRequester());
        assertEquals(TicketStatus.RESOLVED, ticketExample.getStatus());
        assertEquals(TicketPriority.LOW, ticketExample.getPriority());

        assertEquals(originalOpenDate, ticketExample.getOpenDate());

        verify(ticketRepository).save(ticketExample);

    }

    @Test
    void shouldThrowExceptionWhenUpdatingTicketThatDoesNotExist() {

        UpdateTicketRequestDTO requestDTO = updateTicketRequestDTOExample();

        when(ticketRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.updateTicket(999L, requestDTO)
        );

    }

    @Test
    void shouldDeleteTicketWhenIdExists() {

        Ticket ticketExample = ticketExample1();

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticketExample));

        ticketService.deleteTicketById(1L);

        verify(ticketRepository).delete(ticketExample);

    }

    @Test
    void shouldThrowExceptionWhenDeleteIdDoesNotExist() {
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.deleteTicketById(1L)
        );
    }

    @Test
    void shouldReturnTicketsByStatus() {

        Ticket ticket1 = ticketExample1();
        Ticket ticket2 = ticketExample2();

        when(ticketRepository.findByStatus(TicketStatus.OPEN))
                .thenReturn(List.of(ticket1, ticket2));

        List<Ticket> tickets = ticketService.getTicketsByStatus(TicketStatus.OPEN);

        assertEquals(2, tickets.size());
        assertEquals(ticket1, tickets.get(0));
        assertEquals(ticket2, tickets.get(1));

        verify(ticketRepository).findByStatus(TicketStatus.OPEN);

    }

    @Test
    void shouldReturnTicketsByPriority() {

        Ticket ticket1 = ticketExample1();
        Ticket ticket2 = ticketExample2();

        when(ticketRepository.findByPriority(TicketPriority.CRITICAL))
                .thenReturn(List.of(ticket1, ticket2));

        List<Ticket> tickets = ticketService.getTicketsByPriority(TicketPriority.CRITICAL);

        assertEquals(2, tickets.size());
        assertEquals(ticket1, tickets.get(0));
        assertEquals(ticket2, tickets.get(1));

        verify(ticketRepository).findByPriority(TicketPriority.CRITICAL);

    }

    @Test
    void shouldReturnTicketsByStatusAndPriority() {

        Ticket ticket1 = ticketExample1();
        Ticket ticket2 = ticketExample2();

        when(ticketRepository.findByStatusAndPriority(TicketStatus.OPEN, TicketPriority.CRITICAL))
                .thenReturn(List.of(ticket1, ticket2));

        List<Ticket> tickets = ticketService.getTicketsByStatusAndPriority(TicketStatus.OPEN, TicketPriority.CRITICAL);

        assertEquals(2, tickets.size());
        assertEquals(ticket1, tickets.get(0));
        assertEquals(ticket2, tickets.get(1));

        verify(ticketRepository).findByStatusAndPriority(TicketStatus.OPEN, TicketPriority.CRITICAL);

    }

    // Métodos auxiliares
    private Ticket ticketExample1() {
        return new Ticket(
                1L,
                "Teste 1",
                "Descrição teste 1",
                "Marcos 1",
                TicketStatus.OPEN,
                TicketPriority.CRITICAL,
                LocalDateTime.now()
        );
    }

    private Ticket ticketExample2() {
        return new Ticket(
                2L,
                "Teste 2",
                "Descrição teste 2",
                "Marcos 2",
                TicketStatus.OPEN,
                TicketPriority.CRITICAL,
                LocalDateTime.now()
        );
    }

    private CreateTicketRequestDTO createTicketRequestDTOExample() {
        return new CreateTicketRequestDTO(
                "Teste",
                "Descrição teste",
                "Marcos",
                TicketPriority.CRITICAL
        );
    }

    private UpdateTicketRequestDTO updateTicketRequestDTOExample() {
        return new UpdateTicketRequestDTO(
                "Teste atualizado",
                "Descrição atualizada",
                "Marcos Atualizado",
                TicketStatus.RESOLVED,
                TicketPriority.LOW
        );
    }

}
