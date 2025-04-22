package com.ddukbbegi.api.store.service;

import com.ddukbbegi.api.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public void get() {
        storeRepository.findByIdOrElseThrow(1L);
    }

}
