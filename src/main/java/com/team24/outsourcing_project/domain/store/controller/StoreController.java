package com.team24.outsourcing_project.domain.store.controller;

import com.team24.outsourcing_project.domain.common.annotation.Auth;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.dto.StoreResponseDto;
import com.team24.outsourcing_project.domain.store.dto.StoreSimpleResponseDto;
import com.team24.outsourcing_project.domain.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    private void createStore(@Auth AuthUser authUser,
            @Valid @RequestBody StoreRequestDto storeRequestDto) {
        storeService.createStore(authUser, storeRequestDto);
    }

    @GetMapping("/stores/{id}")
    private StoreResponseDto getStore(@Auth AuthUser authUser, @PathVariable Long id) {
        StoreResponseDto storeResponseDto = storeService.getStore(authUser, id);
        return storeResponseDto;

    }

    @GetMapping("/stores")
    private List<StoreSimpleResponseDto> getStores(@Auth AuthUser authUser) {
        List<StoreSimpleResponseDto> storeList = storeService.getStores(authUser);
        return storeList;
    }

    @PutMapping("/stores/{storeId}")
    private void updateStore(@Auth AuthUser authUser, @PathVariable Long storeId,
            @Valid @RequestBody StoreRequestDto storeRequestDto) {
        storeService.updateStore(authUser, storeId, storeRequestDto);
    }

    @DeleteMapping("/stores/{storeId}")
    private void deleteStore(@Auth AuthUser authUser, @PathVariable Long storeId) {
        storeService.deleteStore(authUser, storeId);
    }

}
