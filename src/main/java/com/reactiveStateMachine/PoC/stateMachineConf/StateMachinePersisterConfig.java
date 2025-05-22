package com.reactiveStateMachine.PoC.stateMachineConf;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import com.reactiveStateMachine.PoC.domain.Order;
import com.reactiveStateMachine.PoC.domain.OrderEvent;
import com.reactiveStateMachine.PoC.domain.OrderState;

@Configuration
public class StateMachinePersisterConfig {
	
	@Bean
	public StateMachinePersister<OrderState, OrderEvent, Order> orderPersister(){
		return new DefaultStateMachinePersister<>(inMemoryPersist());
	}
	
	@Bean
	public StateMachinePersister<OrderState, OrderEvent, UUID> orderPersister(
			RepositoryBackedStateMachinePersist persist
	) {
	    return new DefaultStateMachinePersister<>(persist);
	}

	
	@Bean
	public StateMachinePersist<OrderState, OrderEvent, Order> inMemoryPersist(){
		return new StateMachinePersist<OrderState, OrderEvent, Order>() {
			
			@Override
			public void write(StateMachineContext<OrderState, OrderEvent> context, Order order) throws Exception {
				order.setState(context.getState());
				
			}
			
			@Override
			public StateMachineContext<OrderState, OrderEvent> read(Order order) throws Exception {
				
				return new DefaultStateMachineContext<>(
	                    order.getState(), null, null, null
	                );
			}
		};
	}

}
