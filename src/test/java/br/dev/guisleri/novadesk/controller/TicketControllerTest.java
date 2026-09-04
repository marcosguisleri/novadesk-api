package br.dev.guisleri.novadesk.controller;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.exception.TicketNotFoundException;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllTickets() throws Exception {

        Ticket ticket1 = ticketExample1();
        Ticket ticket2 = ticketExample2();

        when(ticketService.getAllTickets())
                .thenReturn(List.of(ticket1, ticket2));

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Teste 1"))
                .andExpect(jsonPath("$[1].title").value("Teste 2"));

        verify(ticketService).getAllTickets();

    }

    @Test
    void shouldReturnTicketWhenIdExists() throws Exception {

        Ticket ticket = ticketExample1();

        when(ticketService.getTicketById(1L))
                .thenReturn(ticket);

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Teste 1"));

        verify(ticketService).getTicketById(1L);

    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        when(ticketService.getTicketById(999L))
                .thenThrow(new TicketNotFoundException(999L));

        mockMvc.perform(get("/tickets/999"))
                .andExpect(status().isNotFound());

        verify(ticketService).getTicketById(999L);

    }

    @Test
    void shouldCreateTicketAndReturnCreated() throws Exception {

        CreateTicketRequestDTO requestDTO = createTicketRequestDTOExample();
        Ticket ticket = ticketExample1();

        when(ticketService.createTicket(requestDTO))
                .thenReturn(ticket);

        mockMvc.perform(
                        post("/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Teste 1"));

        verify(ticketService).createTicket(requestDTO);

    }

    @Test
    void shouldReturnBadRequestWhenCreateTicketIsInvalid() throws Exception {

        String invalidJson = """
            {
              "title": "",
              "description": "Descrição teste",
              "requester": "Marcos"
            }
            """;

        mockMvc.perform(
                        post("/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest());

        verify(ticketService, never()).createTicket(any());

    }

    @Test
    void shouldUpdateTicketAndReturnOk() throws Exception {

        UpdateTicketRequestDTO requestDTO = updateTicketRequestDTOExample();
        Ticket ticket = ticketExample1();

        when(ticketService.updateTicket(
                anyLong(),
                any(UpdateTicketRequestDTO.class)
        )).thenReturn(ticket);

        mockMvc.perform(
                        put("/tickets/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Teste 1"));

        verify(ticketService).updateTicket(1L, requestDTO);

    }

    @Test
    void shouldReturnBadRequestWhenUpdateTicketIsInvalid() throws Exception {

        String invalidJson = """
            {
              "title": "",
              "description": "Descrição teste",
              "requester": "Marcos",
              "status": "IN_PROGRESS",
              "priority": "HIGH"
            }
            """;

        mockMvc.perform(
                        put("/tickets/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest());

        verify(ticketService, never())
                .updateTicket(anyLong(), any(UpdateTicketRequestDTO.class));

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
