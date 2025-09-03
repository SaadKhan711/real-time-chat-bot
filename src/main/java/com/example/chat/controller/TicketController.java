package com.example.chat.controller;

import com.example.chat.model.Ticket;
import com.example.chat.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket newTicket) {
        newTicket.setStatus(Ticket.Status.OPEN);
        return ticketRepository.save(newTicket);
    }

    @GetMapping
    public List<Ticket> getTickets(@RequestParam Ticket.Status status) {
        return ticketRepository.findByStatus(status);
    }

    @PutMapping("/{id}/resolve")
    public Ticket resolveTicket(@PathVariable String id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        ticket.setStatus(Ticket.Status.RESOLVED);
        return ticketRepository.save(ticket);
    }
}
