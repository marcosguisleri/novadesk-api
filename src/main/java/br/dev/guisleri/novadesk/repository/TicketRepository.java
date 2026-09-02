package br.dev.guisleri.novadesk.repository;

import br.dev.guisleri.novadesk.model.Ticket;

import java.util.List;

public interface TicketRepository {

    void save(Ticket ticket);

    List<Ticket> findAll();

}
