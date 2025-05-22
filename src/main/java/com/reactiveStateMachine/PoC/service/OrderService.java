package com.reactiveStateMachine.PoC.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.reactiveStateMachine.PoC.domain.Order;
import com.reactiveStateMachine.PoC.domain.OrderEvent;
import com.reactiveStateMachine.PoC.domain.OrderState;
import com.reactiveStateMachine.PoC.persistence.OrderRepository;

import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StateMachineFactory<OrderState, OrderEvent> factory;
    
    public Mono<Order> createOrder() {
        Order order = new Order();
        return orderRepository.save(order);
    }

    public Mono<String> processEvent(UUID orderId, OrderEvent event) {
        return orderRepository.findById(orderId)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found")))
            .flatMap(order -> Mono.defer(() -> {
                StateMachine<OrderState, OrderEvent> sm = factory.getStateMachine(orderId.toString());

                return sm.startReactively()
                    .then(Mono.defer(() ->
                        Mono.fromDirect(
                            sm.getStateMachineAccessor().withRegion().resetStateMachineReactively(
                                new DefaultStateMachineContext<>(
                                    order.getState(), null, null, null, null, orderId.toString()
                                )
                            )
                        )
                    ))
                    .thenMany(sm.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())))
                    .then(Mono.defer(() -> {
                        order.setState(sm.getState().getId());
                        return orderRepository.save(order)
                            .then(sm.stopReactively())
                            .thenReturn("Order state: " + order.getState() + " Id: " + order.getId());
                    }));
            }));
    }


}

