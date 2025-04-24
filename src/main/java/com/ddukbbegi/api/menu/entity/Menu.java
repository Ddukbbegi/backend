package com.ddukbbegi.api.menu.entity;

import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int price;

	private String description;

	private Category category;

	private MenuStatus status;

	@Column(name = "is_option")
	private boolean isOption;

	@Column(name = "store_id")
	private long storeId;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@Builder
	public Menu(String name, int price, String description, Category category, boolean isOption, MenuStatus status, long storeId) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.isOption = isOption;
		this.status = status;
		this.storeId = storeId;
	}

	public void update(String name, int price, String description, Category category) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
	}

}
