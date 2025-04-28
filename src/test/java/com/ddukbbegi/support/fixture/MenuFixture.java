package com.ddukbbegi.support.fixture;

import com.ddukbbegi.api.menu.dto.request.NewMenuRequestDto;
import com.ddukbbegi.api.menu.enums.Category;
import com.ddukbbegi.api.menu.enums.MenuStatus;

public class MenuFixture {

    public static NewMenuRequestDto createNewMenuRequestDto(String menuName) {
        return new NewMenuRequestDto(
                menuName,
                1000,
                "description",
                Category.MAIN_MENU,
                MenuStatus.ON_SALE
        );
    }

}
