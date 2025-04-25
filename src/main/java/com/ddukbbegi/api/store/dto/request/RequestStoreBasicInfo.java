package com.ddukbbegi.api.store.dto.request;

import com.ddukbbegi.api.store.enums.StoreCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestStoreBasicInfo(
        @NotBlank
        @Size(min = 1, max = 20)
        String name,

        String category,
        @Pattern(
                regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
                message = "전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678 또는 02-123-4567"
        ) String phoneNumber,

        @NotBlank(message = "가게 설명을 입력해주세요.")
        @Size(max = 1000, message = "설명은 최대 1000자까지 입력할 수 있습니다.")
        String description
) {

    public StoreCategory getCategory() {
        return StoreCategory.fromString(this.category);
    }

}
