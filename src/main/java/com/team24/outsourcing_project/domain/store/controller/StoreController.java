package com.team24.outsourcing_project.domain.store.controller;

import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.dto.StoreResponseDto;
import com.team24.outsourcing_project.domain.store.dto.StoreSimpleResponseDto;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/stores")
    private void createStore(@RequestBody StoreRequestDto storeRequestDto)
    {
        storeService.createStore(storeRequestDto);
    }
    @GetMapping("/stores/{id}")
    private StoreResponseDto getStore(@PathVariable Long id)
    {
        Store store = storeService.getStore(id);
        StoreResponseDto storeResponseDto = StoreResponseDto.of(store.getUser().getId(),store.getName(),store.getMinOrderPrice(),store.getOpenTime(),store.getCloseTime(),store.getMenuList());
        return storeResponseDto;

    }
    @GetMapping("/stores")
    private List<StoreSimpleResponseDto> getStores()
    {
        List<Store> storeList = storeService.getStores();
        return storeList.stream().map(store -> StoreSimpleResponseDto.of(store.getId(), store.getName(), store.getMinOrderPrice(),
                store.getOpenTime(),store.getCloseTime())).collect(Collectors.toList());
    }

    @PutMapping("/stores/{storeId}")
    private void updateStore(@RequestBody StoreRequestDto storeRequestDto, @PathVariable Long id)
    {
        storeService.updateStore(storeRequestDto,id);
    }

}
