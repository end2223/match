package org.example;

import java.util.List;

public class Match {
    private String id;
    private int numberOfTicket;
    private List<Ticket> ticketList;

    public Match(String id, int numberOfTicket, List<Ticket> ticketList) {
        this.id = id;
        this.numberOfTicket = numberOfTicket;
        this.ticketList = ticketList;
    }

    public Match() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberOfTicket() {
        return numberOfTicket;
    }

    public void setNumberOfTicket(int numberOfTicket) {
        this.numberOfTicket = numberOfTicket;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }
}
