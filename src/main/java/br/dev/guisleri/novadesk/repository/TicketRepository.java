package br.dev.guisleri.novadesk.repository;

import br.dev.guisleri.novadesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
