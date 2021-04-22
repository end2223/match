package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ApplicationScoped
public class BookingRepository {

    private final MongoCollection<Booking> collection;

    public BookingRepository(){
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
        collection = mongoDatabase.getCollection("booking", Booking.class);
    }

    public Mono<Booking> addBooking(Booking booking){
        return Mono.from(collection.insertOne(booking))
                .then(Mono.just(booking));
    }

    public Mono<Booking> findBookingById(String bookingId){
        return Mono.from(collection.find(Filters.eq("_id", bookingId)).first());
    }
    
}
