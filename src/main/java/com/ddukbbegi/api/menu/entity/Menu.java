package com.ddukbbegi.api.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int price;

	private String description;

	private String category;

	@Column(name = "is_option")
	private boolean isOption;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@Column(name = "store_id")
	private long storeId;

	@Builder
	public Menu(String name, int price, String description, String category, boolean isOption, long storeId) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.isOption = isOption;
		this.storeId = storeId;
	}

	public Menu() {

	}

	public void update(String name, int price, String description, String category) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
	}

	public void delete() {
		this.isDeleted = true;
	}
}
