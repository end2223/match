package org.example;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    private String id;
    private int numberOfTicket;
    private String name;
    private String date;
    private List<Ticket> ticketList;

}
