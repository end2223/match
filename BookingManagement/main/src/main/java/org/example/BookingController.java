package org.example;

import com.google.gson.Gson;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    @Inject
    BookingRepository bookingRepository;

    @POST()
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public Uni<Void> addBookingApi(String booking){
        Booking B = (new Gson()).fromJson(booking, Booking.class);
        System.out.println(B.getMatchId()+"M"+B.getTicketId()+"T"+B.getCustomerId()+"C");
        return Uni.createFrom().publisher(addBooking(B));
    }
    private Mono<Void> addBooking(Booking booking){
        return bookingRepository.addBooking(booking)
                    .switchIfEmpty(Mono.error(new Throwable("error")))
                    .flatMap(booking1 -> setStatusBookedTicket(booking1));
    }

    private Mono<Void> setStatusBookedTicket(Booking booking){
        WebClient client = WebClient.create("http://localhost:8092");
        return client.post()
                .uri("/matches/ticket/changeStatus/" + booking.getMatchId() + "/" + booking.getTicketId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(Mono.just(booking), Booking.class)
                .retrieve()
                .bodyToMono(Match.class)
                .then();
    }

}
