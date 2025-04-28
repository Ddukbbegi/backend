package com.ddukbbegi.api.menu.entity;
import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Builder
	public Menu(String name, int price, String description, Category category, MenuStatus status, Store store) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.status = status;
		this.store = store;
	}

	public void update(String name, int price, String description, Category category) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
	}

}