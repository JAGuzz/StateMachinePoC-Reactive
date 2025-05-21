package com.reactiveStateMachine.PoC.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("orders_table")
@NoArgsConstructor
@Getter
@Setter
public class Order {
	
	@Id
    private UUID id = UUID.randomUUID();
    
    
    private OrderState state;
    
    public Order(OrderState state) {
		this.state = state;
	}

	public void setState(OrderState state) {
		this.state = state;
		
	}

	public OrderState getState() {
		
		return this.state;
	}

	public UUID getId() {
		return id;
	}
	
	

}
