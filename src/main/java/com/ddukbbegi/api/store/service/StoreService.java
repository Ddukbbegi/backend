package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreIdResponseDto registerStore(StoreRegisterRequestDto dto) {

        if (!storeRepository.isStoreRegistrationAvailable(1L)) {    // TODO: user 연동
            throw new BusinessException(ResultCode.STORE_LIMIT_EXCEEDED);
        }

        User user = userRepository.findByIdOrElseThrow(1L); // TODO: user 연동
        Store entity = dto.toEntity(user);
        Store savedStore = storeRepository.save(entity);

        return StoreIdResponseDto.of(savedStore.getId());
    }

    public void get() {
        storeRepository.findByIdOrElseThrow(1L);
    }

}
