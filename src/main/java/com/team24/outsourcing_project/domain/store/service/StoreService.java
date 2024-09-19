package com.team24.outsourcing_project.domain.store.service;

import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.dto.StoreResponseDto;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public void createStore(StoreRequestDto storeRequestDto) {

    }

    public Store getStore(Long id) {
       Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("가게를 찾지 못했습니다."));
       return store;
    }

    public List<Store> getStores() {
        List<Store> stores = storeRepository.findAll();
        return stores;
    }

    public void updateStore(StoreRequestDto storeRequestDto, Long id) {
        Store store = storeRepository.
    }
}
