package com.reactiveStateMachine.PoC.stateMachineConf;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import com.reactiveStateMachine.PoC.domain.Order;
import com.reactiveStateMachine.PoC.domain.OrderEvent;
import com.reactiveStateMachine.PoC.domain.OrderState;
import com.reactiveStateMachine.PoC.persistence.OrderRepository;

@Component
public class RepositoryBackedStateMachinePersist implements StateMachinePersist<OrderState, OrderEvent, UUID> {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public void write(StateMachineContext<OrderState, OrderEvent> context, UUID orderId) throws Exception {
		
		Order order = orderRepository.findById(orderId).block();
		
		if(order != null) {
			order.setState(context.getState());
			orderRepository.save(order).block();
		}

	}

	@Override
	public StateMachineContext<OrderState, OrderEvent> read(UUID orderId) throws Exception {
		Order order = orderRepository.findById(orderId).block();
		
		if(order==null) throw new IllegalStateException("Order Not Found");
		
		return new DefaultStateMachineContext<>(order.getState(), null, null, null, null, orderId.toString());
	}

}
