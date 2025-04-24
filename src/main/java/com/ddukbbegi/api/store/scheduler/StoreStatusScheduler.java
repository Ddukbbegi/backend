package com.ddukbbegi.api.store.scheduler;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.store.service.StoreStatusResolveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class StoreStatusScheduler {

    private final StoreRepository storeRepository;
    private final StoreStatusResolveService storeStatusResolveService;

    @Scheduled(cron = "0 */10 * * * *")     // 매 10분마다 실행
    @Transactional
    public void updateStoreStatuses() {

        List<Store> stores = storeRepository.findAll();
        long updatedRows = 0L;

        LocalDateTime now = LocalDateTime.now();
        for (Store store : stores) {
            StoreStatus newStatus = storeStatusResolveService.resolveStoreStatus(store, now);

            if (!Objects.equals(store.getStatus(), newStatus)) {
                store.updateStatus(newStatus);
                updatedRows++;
            }
        }

        log.info("[{}] 가게 status 업데이트 완료: {} 개 처리",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                updatedRows);
    }

}
