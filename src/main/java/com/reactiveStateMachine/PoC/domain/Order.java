package com.reactiveStateMachine.PoC.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table("orders_table")
@Getter
@Setter
public class Order implements Persistable<UUID> {
	
	@Id
    private UUID id;
    
    
    private OrderState state;
    
    @Transient
    private boolean isNew;
    
    public Order() {
        this.id = UUID.randomUUID();
        this.state = OrderState.NEW;
        this.isNew = true;
    }
    
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }

	public void setState(OrderState state) {
		this.state = state;
		
	}

	public OrderState getState() {
		
		return this.state;
	}
	

}
