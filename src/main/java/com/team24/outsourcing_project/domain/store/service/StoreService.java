package com.team24.outsourcing_project.domain.store.service;

import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.dto.StoreResponseDto;
import com.team24.outsourcing_project.domain.store.dto.StoreSimpleResponseDto;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.entity.StoreStatus;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public void createStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        if(user.getRole() == UserRole.OWNER) {
            Store store = Store.createStore(storeRequestDto.getName(), storeRequestDto.getMinOrderPrice(), true, StoreStatus.OPEN, storeRequestDto.getOpenTime(), storeRequestDto.getCloseTime(), user);
            storeRepository.save(store);
        }
        else
            throw new RuntimeException("사장이 아닙니다.");
    }

    public StoreResponseDto getStore(AuthUser authUser, Long id) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        Store store = storeRepository.findByIdAndRole(id,StoreStatus.OPEN);
        StoreResponseDto storeResponseDto = StoreResponseDto.of(store.getUser().getId(), store.getName(), store.getMinOrderPrice(), store.getOpenTime(), store.getCloseTime(), store.getMenuList());
        return storeResponseDto;
    }

    public List<StoreSimpleResponseDto> getStores(AuthUser authUser) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        List<Store> storeList = storeRepository.findAllByUserIdAndRole(user.getId(),StoreStatus.OPEN);
        return storeList.stream().map(store -> StoreSimpleResponseDto.of(store.getId(), store.getName(), store.getMinOrderPrice(),
                store.getOpenTime(), store.getCloseTime())).collect(Collectors.toList());
    }

    public void updateStore(AuthUser authUser,Long id, StoreRequestDto storeRequestDto) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("가게를 찾지 못했습니다."));
        store.update(storeRequestDto.getName(),store.getMinOrderPrice(),store.getOpenTime(),store.getCloseTime());
        storeRepository.save(store);
    }

    public void deleteStore(AuthUser authUser, Long id) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("가게를 찾지 못했습니다."));
        store.changeStatus(StoreStatus.OUT);
        storeRepository.save(store);
    }
}
