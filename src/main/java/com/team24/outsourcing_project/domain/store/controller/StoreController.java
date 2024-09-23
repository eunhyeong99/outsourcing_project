package com.team24.outsourcing_project.domain.store.controller;

import com.team24.outsourcing_project.domain.common.annotation.Auth;
import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.dto.StoreResponseDto;
import com.team24.outsourcing_project.domain.store.dto.StoreSimpleResponseDto;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.service.StoreService;
import jakarta.validation.Valid;
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
    private void createStore(@Auth AuthUser authUser, @Valid @RequestBody StoreRequestDto storeRequestDto)
    {
        storeService.createStore(authUser,storeRequestDto);
    }
    @GetMapping("/stores/{id}")
    private StoreResponseDto getStore(@Auth AuthUser authUser, @PathVariable Long id)
    {
        StoreResponseDto storeResponseDto = storeService.getStore(authUser,id);
        return storeResponseDto;

    }
    @GetMapping("/stores")
    private List<StoreSimpleResponseDto> getStores(@Auth AuthUser authUser)
    {
        List<StoreSimpleResponseDto> storeList = storeService.getStores(authUser);
        return storeList;
    }

    @PutMapping("/stores/{storeId}")
    private void updateStore(@Auth AuthUser authUser,@PathVariable Long storeId, @Valid @RequestBody StoreRequestDto storeRequestDto)
    {
        storeService.updateStore(authUser,storeId,storeRequestDto);
    }

    @DeleteMapping("/stores/{storeId}")
    private void deleteStore(@Auth AuthUser authUser, @PathVariable Long storeId)
    {
        storeService.deleteStore(authUser,storeId);
    }

}
