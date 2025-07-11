package com.ddukbbegi.api.menu.entity;

import java.util.List;

import com.ddukbbegi.api.menu.enums.MenuStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "options")
public class Option {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int price;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MenuStatus status;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Menu menu;

	@Builder
	public Option(String name, int price, MenuStatus status, Menu menu) {
		this.name = name;
		this.price = price;
		this.status = status;
		this.menu = menu;
	}

	public void update(String name, int price) {
		this.name = name;
		this.price = price;
	}
}