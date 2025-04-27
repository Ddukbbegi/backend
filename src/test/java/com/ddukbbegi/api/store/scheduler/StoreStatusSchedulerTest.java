package com.ddukbbegi.api.store.scheduler;

import com.ddukbbegi.api.store.entity.Store;
import com.ddukbbegi.api.store.enums.StoreStatus;
import com.ddukbbegi.api.store.repository.StoreRepository;
import com.ddukbbegi.api.store.service.StoreStatusResolveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreStatusSchedulerTest {

    @InjectMocks private StoreStatusScheduler storeStatusScheduler;

    @Mock private StoreRepository storeRepository;
    @Mock private StoreStatusResolveService storeStatusResolveService;

    @DisplayName("상태가 변경된 경우 updateStatus가 호출된다")
    @Test
    void givenChangedStatus_whenUpdateStoreStatuses_thenStatusIsUpdated() {
        // given
        Store store = mock(Store.class);

        given(storeRepository.findAll()).willReturn(List.of(store));
        given(store.getStatus()).willReturn(StoreStatus.CLOSED);
        given(storeStatusResolveService.resolveStoreStatus(any(), any())).willReturn(StoreStatus.OPEN);

        // when
        storeStatusScheduler.updateStoreStatuses();

        // then
        verify(store).updateStatus(StoreStatus.OPEN);
    }

    @DisplayName("상태가 동일하면 updateStatus가 호출되지 않는다")
    @Test
    void givenUnchangedStatus_whenUpdateStoreStatuses_thenStatusIsNotUpdated() {
        // given
        Store store = mock(Store.class);

        given(storeRepository.findAll()).willReturn(List.of(store));
        given(store.getStatus()).willReturn(StoreStatus.OPEN);
        given(storeStatusResolveService.resolveStoreStatus(any(), any())).willReturn(StoreStatus.OPEN);

        // when
        storeStatusScheduler.updateStoreStatuses();

        // then
        verify(store, never()).updateStatus(any());
    }

}
