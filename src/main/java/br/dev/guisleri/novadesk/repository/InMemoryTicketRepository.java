package br.dev.guisleri.novadesk.repository;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final List<Ticket> tickets = new ArrayList<>();

    public InMemoryTicketRepository() {

        tickets.add(new Ticket(
                1L,
                "Notebook sem acesso à internet",
                "Usuário conecta ao Wi-Fi, mas não consegue acessar sites externos.",
                "João Silva",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                LocalDateTime.now()
        ));

        tickets.add(new Ticket(
                2L,
                "Impressora não imprime em cores",
                "A impressora está imprimindo apenas em preto e branco mesmo com toner colorido disponível.",
                "Mariana Costa",
                TicketStatus.IN_PROGRESS,
                TicketPriority.MEDIUM,
                LocalDateTime.now()
        ));

        tickets.add(new Ticket(
                3L,
                "Erro ao acessar sistema financeiro",
                "Ao tentar realizar login, o sistema retorna mensagem de usuário não autorizado.",
                "Carlos Mendes",
                TicketStatus.OPEN,
                TicketPriority.CRITICAL,
                LocalDateTime.now()
        ));

        tickets.add(new Ticket(
                4L,
                "Instalação do Microsoft Teams",
                "Solicitada instalação do Microsoft Teams no notebook do colaborador.",
                "Ana Souza",
                TicketStatus.RESOLVED,
                TicketPriority.LOW,
                LocalDateTime.now()
        ));
    }

    private Long id = 5L;

    @Override
    public void save(Ticket ticket) {
        ticket.setId(id++);
        tickets.add(ticket);
    }

    @Override
    public Ticket update(long id, Ticket updatedTicket) {
        Optional<Ticket> foundTicket = findById(id);

        if (foundTicket.isEmpty()) {
            return null;
        }

        Ticket ticket = foundTicket.get();

        ticket.setTitle(updatedTicket.getTitle());
        ticket.setDescription(updatedTicket.getDescription());
        ticket.setRequester(updatedTicket.getRequester());
        ticket.setStatus(updatedTicket.getStatus());
        ticket.setPriority(updatedTicket.getPriority());

        return ticket;
    }

    @Override
    public boolean deleteById(long id) {
        return tickets.removeIf(ticket -> ticket.getId() == id);
    }

    @Override
    public List<Ticket> findAll() {
        return tickets;
    }

    @Override
    public Optional<Ticket> findById(long id) {
        return tickets.stream()
                .filter(ticket -> ticket.getId() == id)
                .findFirst();
    }

}
