package com.ddukbbegi.api.menu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class MenuOption {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Menu menu;

	@ManyToOne
	private Option option;
}
