package com.reactiveStateMachine.PoC.persistence;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.reactiveStateMachine.PoC.domain.Order;

public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {

}
