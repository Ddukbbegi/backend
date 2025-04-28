package com.ddukbbegi.api.menu.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuRequestDto;
import com.ddukbbegi.api.menu.dto.request.UpdatingMenuStatusRequestDto;
import com.ddukbbegi.api.menu.dto.response.AllMenuResponseDto;
import com.ddukbbegi.api.menu.dto.response.DetailMenuResponseDto;
import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;
import com.ddukbbegi.api.menu.repository.MenuRepository;
import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.repository.StoreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private MenuServiceImpl menuService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void findAllMenusForOwner_success() {
		// given
		long storeId = 1L;
		long userId = 1L;

		Store store = mock(Store.class);
		Menu menu1 = mock(Menu.class);
		Menu menu2 = mock(Menu.class);

		when(menuRepository.isStoreOwner(storeId, userId)).thenReturn(true);
		when(storeRepository.findByIdOrElseThrow(storeId)).thenReturn(store);
		when(menuRepository.isStoreOwner(storeId, userId)).thenReturn(true);
		when(menuRepository.findAllByStoreId(storeId)).thenReturn(Arrays.asList(menu1, menu2));

		// when
		List<AllMenuResponseDto> result = menuService.findAllMenusForOwner(storeId, userId);

		// then
		assertThat(result).hasSize(2);
		verify(menuRepository).isStoreOwner(storeId, userId);
		verify(menuRepository).findAllByStoreId(storeId);
	}

	@Test
	void findAllMenusForOwner_notOwner_throwsException() {
		// given
		long storeId = 1L;
		long userId = 2L;

		when(menuRepository.isStoreOwner(storeId, userId)).thenReturn(false);

		// when / then
		assertThrows(RuntimeException.class, () -> menuService.findAllMenusForOwner(storeId, userId));
		verify(menuRepository).isStoreOwner(storeId, userId);
	}

	@Test
	void findAllMenusForCustomer_success() {
		// given
		long storeId = 1L;
		Store store = mock(Store.class); // store mock 객체 생성
		Menu menu1 = mock(Menu.class);
		Menu menu2 = mock(Menu.class);

		when(storeRepository.findByIdOrElseThrow(storeId)).thenReturn(store);

		when(menuRepository.findAllByStoreIdAndStatusNot(storeId, MenuStatus.DELETED))
			.thenReturn(List.of(menu1, menu2));

		// when
		List<AllMenuResponseDto> result = menuService.findAllMenusForCustomer(storeId);

		// then
		assertThat(result).hasSize(2);
		verify(menuRepository).findAllByStoreIdAndStatusNot(storeId, MenuStatus.DELETED);
	}

	@Test
	void addNewMenu_success() {
		// given
		long userId = 1L;
		long storeId = 1L;
		NewMenuRequestDto dto = new NewMenuRequestDto("MenuName", 1000, "description", Category.MAIN_MENU, MenuStatus.ON_SALE);
		Store store = mock(Store.class);
		Menu menu = mock(Menu.class);

		when(menuRepository.isStoreOwner(storeId, userId)).thenReturn(true);
		when(storeRepository.findByIdOrElseThrow(storeId)).thenReturn(store);
		when(menuRepository.save(any(Menu.class))).thenReturn(menu);
		when(menu.getId()).thenReturn(1L);

		// when
		Long result = menuService.addNewMenu(userId, storeId, dto);

		// then
		assertThat(result).isEqualTo(1L);
		verify(menuRepository).isStoreOwner(storeId, userId);
		verify(storeRepository).findByIdOrElseThrow(storeId);
		verify(menuRepository).save(any(Menu.class));
	}

	@Test
	void updateMenu_success() {
		// given
		long userId = 1L;
		long menuId = 1L;
		UpdatingMenuRequestDto dto = new UpdatingMenuRequestDto("UpdatedMenu", 2000, "UpdatedDesc", Category.MAIN_MENU);

		Menu menu = mock(Menu.class);

		when(menuRepository.isMenuOwner(menuId, userId)).thenReturn(true);
		when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

		// when
		menuService.updateMenu(userId, menuId, dto);

		// then
		verify(menuRepository).isMenuOwner(menuId, userId);
		verify(menuRepository).findById(menuId);
		verify(menu).update(dto.name(), dto.price(), dto.description(), dto.category());
	}

	@Test
	void updateMenu_menuNotFound_throwsException() {
		// given
		long userId = 1L;
		long menuId = 1L;
		UpdatingMenuRequestDto dto = new UpdatingMenuRequestDto("UpdatedMenu", 2000, "UpdatedDesc", Category.MAIN_MENU);

		when(menuRepository.isMenuOwner(menuId, userId)).thenReturn(true);
		when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

		// when / then
		assertThrows(NoSuchElementException.class, () -> menuService.updateMenu(userId, menuId, dto));
		verify(menuRepository).isMenuOwner(menuId, userId);
		verify(menuRepository).findById(menuId);
	}

	@Test
	void updateMenu_notOwner_throwsException() {
		// given
		long userId = 1L;
		long menuId = 1L;

		when(menuRepository.isMenuOwner(menuId, userId)).thenReturn(false);

		// when / then
		assertThrows(RuntimeException.class, () -> menuService.updateMenu(userId, menuId,
			new UpdatingMenuRequestDto("UpdatedMenu", 2000, "UpdatedDesc", Category.MAIN_MENU)));
		verify(menuRepository).isMenuOwner(menuId, userId);
	}

	@Test
	void switchMenuStatus_success() {
		// given
		long userId = 1L;
		long menuId = 1L;
		UpdatingMenuStatusRequestDto dto = new UpdatingMenuStatusRequestDto(MenuStatus.SOLD_OUT);

		when(menuRepository.isMenuOwner(menuId, userId)).thenReturn(true);

		// when
		menuService.switchMenuStatus(userId, menuId, dto);

		// then
		verify(menuRepository).isMenuOwner(menuId, userId);
		verify(menuRepository).updateMenuStatusById(menuId, MenuStatus.SOLD_OUT);
	}

	@Test
	void switchMenuStatus_notOwner_throwsException() {
		// given
		long userId = 1L;
		long menuId = 1L;
		UpdatingMenuStatusRequestDto dto = new UpdatingMenuStatusRequestDto(MenuStatus.SOLD_OUT);

		when(menuRepository.isMenuOwner(menuId, userId)).thenReturn(false);

		// when / then
		assertThrows(RuntimeException.class, () -> menuService.switchMenuStatus(userId, menuId, dto));
		verify(menuRepository).isMenuOwner(menuId, userId);
	}
}