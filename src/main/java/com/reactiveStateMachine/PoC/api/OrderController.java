package com.reactiveStateMachine.PoC.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactiveStateMachine.PoC.domain.Order;
import com.reactiveStateMachine.PoC.domain.OrderEvent;
import com.reactiveStateMachine.PoC.service.OrderService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("")
	public Mono<Order> createOrder(){
		return orderService.createOrder();
	}

	@PostMapping("/event")
	public Mono<String> processEvent(@RequestBody UUID id, @RequestBody OrderEvent event) {
		return orderService.processEvent(id, event);
	}

}
