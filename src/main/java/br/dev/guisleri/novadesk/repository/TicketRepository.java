package br.dev.guisleri.novadesk.repository;

import br.dev.guisleri.novadesk.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    void save(Ticket ticket);

    Ticket update(long id, Ticket updatedTicket);

    List<Ticket> findAll();

    Optional<Ticket> findById(long id);

}
