package org.example;

import com.google.gson.Gson;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Slf4j
@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchController {

    @Inject
    MatchRepository matchRepository;

    @POST()
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public Uni<Match> addMatchApi(String match){
        Match m = (new Gson()).fromJson(match, Match.class);
        return Uni.createFrom().publisher(addMatch(m));
    }
    private Mono<Match> addMatch(Match match){
        match.setId(UUID.randomUUID().toString());
        return matchRepository.findMatchById(match.getId())
                .flatMap(match1 -> {
                    log.info("Found");
                    return Mono.error(new Throwable("Match existed"));
                })
                .switchIfEmpty(matchRepository.addMatch(match)).then(getMatchById(match.getId()));
    }
    @GET
    @Path("/get/{matchId}")
    public Uni<Match> getMatchByIdApi(@PathParam("matchId") String matchId){
        return Uni.createFrom().publisher(getMatchById(matchId));
    }
    private Mono<Match> getMatchById(String matchId){
        return matchRepository.findMatchById(matchId);
    }
    @POST()
    @Path("/ticket/changeStatus/{matchId}/{ticketId}")
    public Uni<Match> changeTicketStatusApi(@PathParam("matchId") String matchId, @PathParam("ticketId")String ticketId){
        return Uni.createFrom().publisher(changeTicketStatus(matchId, ticketId));
    }

    private Mono<Match> changeTicketStatus(String matchId, String ticketId){
        return matchRepository.findMatchById(matchId)
                .switchIfEmpty(Mono.error(new Throwable("Could not find match id " + matchId)))
                .flatMap(match -> matchRepository.changeTicketStatus(matchId, ticketId));
    }

    @GET
    @Path("/get/available/{matchId}")
    public Multi<Ticket> getAvailableTicketApi(@PathParam("matchId") String matchId){
        return Multi.createFrom().publisher(getAvailableTicket(matchId));
    }
    private Flux<Ticket> getAvailableTicket(String matchId){
        return matchRepository.findAvailableTicket(matchId);
    }

    @GET
    @Path("/get/available/{matchId}/{ticketId}")
    public Multi<Ticket> getOneAvailableTicketApi(@PathParam("matchId") String matchId, @PathParam("ticketId") String ticketId){
        return Multi.createFrom().publisher(getOneAvailableTicket(matchId, ticketId));
    }
    private Flux<Ticket> getOneAvailableTicket(String matchId, String ticketId){
        System.out.println(matchRepository.findOneAvailableTicket(matchId, ticketId).blockFirst().getId());
        return matchRepository.findOneAvailableTicket(matchId, ticketId);
    }

    @GET
    @Path("")
    public Multi<Match> getAllMatchsApi(){
        return Multi.createFrom().publisher(getAllMatch());
    }
    private Flux<Match> getAllMatch() {
        return matchRepository.findAllMatch();
    }

    @GET
    @Path("/ticket/get/{matchId}/{ticketId}")
    public Uni<Match> getCustomerByIdApi(@PathParam(("matchId")) String matchId, @PathParam("ticketId") String ticketId){
        return Uni.createFrom().publisher(getTicketById(matchId, ticketId));
    }
    private Mono<Match> getTicketById(String matchId, String ticketId){
        return matchRepository.findMatchById(matchId)
                .switchIfEmpty(Mono.error(new Throwable("Could not find match id " + matchId)))
                .flatMap(math->matchRepository.FUTickeAvailabletById(matchId, ticketId));
    }
}
