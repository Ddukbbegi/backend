package com.ddukbbegi.api.menu.entity;

import java.util.ArrayList;
import java.util.List;

import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.store.entity.Store;

import jakarta.persistence.CascadeType;
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

	@Column(name = "is_option")
	private boolean isOption;

	@Column(name = "store_id")
	private long storeId;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "store_id", nullable = false)
	// private Store store;

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
