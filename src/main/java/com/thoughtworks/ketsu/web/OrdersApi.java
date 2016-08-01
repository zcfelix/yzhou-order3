package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.domain.user.UserRepository;
import com.thoughtworks.ketsu.web.exception.InvalidParameterException;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("users/{userId}/orders")

public class OrdersApi {
    @Context
    Routes routes;

    @Context
    UserRepository userRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(Map<String, Object> info,
                                @PathParam("userId") int userId) {
        List<String> invalidParamsList = new ArrayList<>();
        if (info.getOrDefault("name", "").toString().trim().isEmpty())
            invalidParamsList.add("name");
        if (info.getOrDefault("address", "").toString().trim().isEmpty())
            invalidParamsList.add("address");
        if (info.getOrDefault("phone", "").toString().trim().isEmpty())
            invalidParamsList.add("phone");
        if (invalidParamsList.size() > 0)
            throw new InvalidParameterException(invalidParamsList);

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent())
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        Order order = userOptional.get().createOrder(info);
        return Response.created(routes.orderUri(order)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> findAllOrders(@PathParam("userId") int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent())
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        List<Order> ordersList = userOptional.get().findAllOrders();
        return ordersList;
    }

    @Path("{orderId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Order findOrderById(@PathParam("userId") int userId,
                                  @PathParam("orderId") int orderId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent())
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return userOptional.get().findOrderById(orderId).orElseThrow(() -> new NotFoundException("order not found"));
    }
}
