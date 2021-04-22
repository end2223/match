import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    CustomerRepository customerRepository;

    @POST()
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public Uni<Customer> addCustomerApi(String customer){
        System.out.println(customer);
        Customer c = (new Gson()).fromJson(customer,Customer.class);
        System.out.println(c.getName());
        return Uni.createFrom().publisher(addCustomer(c));
    }
    private Mono<Customer> addCustomer(Customer customer){
        //customer.setId(UUID.randomUUID().toString());
        return customerRepository.findCustomerById(customer.getId())
                .flatMap(customer1 -> Mono.error(new Throwable("Customer existed")))
                .switchIfEmpty(customerRepository.addCustomer(customer)).then(getCustomerById(customer.getId()));
    }
    @GET
    @Path("/get/{customerId}")
    public Uni<Customer> getCustomerByIdApi(@PathParam("customerId") String customerId){
        return Uni.createFrom().publisher(getCustomerById(customerId));
    }
    private Mono<Customer> getCustomerById(String customerId){
        return customerRepository.findCustomerById(customerId);
    }
    @GET
    @Path("")
    public Multi<Customer> getAllCustomersApi(){
        return Multi.createFrom().publisher(getAllCustomers());
    }
    private Flux<Customer> getAllCustomers(){
        return customerRepository.findAllCustomers();
    }

}
