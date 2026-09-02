package br.dev.guisleri.novadesk.model;

import java.time.LocalDateTime;

public class Ticket {

    private Long id;
    private String title;
    private String description;
    private String requester;
    private TicketStatus status;
    private TicketPriority prioridade;
    private LocalDateTime openDate;

    public Ticket(Long id, String title, String description, String requester, TicketStatus status, TicketPriority prioridade, LocalDateTime openDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.status = status;
        this.prioridade = prioridade;
        this.openDate = openDate;
    }

    public Ticket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(TicketPriority prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }
}
