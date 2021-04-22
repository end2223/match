package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
@ApplicationScoped
@Slf4j
public class MatchRepository {

    private final MongoCollection<Match> collection;

    public MatchRepository(){
        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        ConnectionString connectionString = new ConnectionString("mongodb+srv://cns2122:lala1234@cluster0.r2852.mongodb.net/test");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .codecRegistry(codecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("management");
        collection = mongoDatabase.getCollection("match", Match.class);
    }

    public Mono<Match> addMatch(Match match){
        return Mono.from(collection.insertOne(match))
                .then(Mono.just(match));
    }

    public Mono<Match> findMatchById(String matchId){
        return Mono.from(collection.find(Filters.eq("_id", matchId)).first());
    }

    public Mono<Match> changeTicketStatus(String matchId, String ticketId){
        List<Bson> filters = new ArrayList<>();
        Bson filter1 = Filters.eq("_id", matchId);
        Bson filter2 = Filters.eq("ticketList._id", ticketId);
        filters.add(filter1);
        filters.add(filter2);
        Bson update = Updates.set("ticketList.$.status", false);
        return Mono.from(collection.updateOne(Filters.and(filters), update))
                .then(findMatchById(matchId));
    }

    public Flux<Ticket> findAvailableTicket(String matchId){
        Bson filter = Filters.eq("_id", matchId);
        return Flux.from(collection.find(filter).first())
                .flatMap(match -> Flux.fromIterable(match.getTicketList())
                    .filter(ticket -> ticket.getStatus().equals(true)));
    }

    public Flux<Ticket> findOneAvailableTicket(String matchId, String ticketId){
        Bson filter = Filters.eq("_id", matchId);
        return Flux.from(collection.find(filter).first())
                .flatMap(match -> Flux.fromIterable(match.getTicketList())
                        .filter(ticket -> ticket.getStatus().equals(true)))
                        .filter(ticket -> ticket.getId().equals(ticketId));
    }

    public Flux<Match> findAllMatch(){
        return Flux.from(collection.find());
    }

    public Mono<Match> FUTickeAvailabletById(String matchId, String ticketId){
        List<Bson> filters = new ArrayList<>();
        Bson filter1 = Filters.eq("_id", matchId);
        Bson filter2 = Filters.eq("ticketList._id", ticketId);
        Bson filter3 = Filters.eq("ticketList.status", true);
        filters.add(filter1);
        filters.add(filter2);
        filters.add(filter3);

        Bson update = Updates.set("ticketList.$.status", false);
        return Mono.from(collection.updateOne(Filters.and(filters), update))
                .then(findMatchById(matchId));
    }
}
