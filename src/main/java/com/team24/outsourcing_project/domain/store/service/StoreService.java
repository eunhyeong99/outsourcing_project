package com.team24.outsourcing_project.domain.store.service;

import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.menu.repository.MenuRepository;
import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.dto.StoreResponseDto;
import com.team24.outsourcing_project.domain.store.dto.StoreSimpleResponseDto;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.entity.StoreLimit;
import com.team24.outsourcing_project.domain.store.entity.StoreStatus;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import com.team24.outsourcing_project.exception.ApplicationException;
import com.team24.outsourcing_project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.embedded.TomcatVirtualThreadsWebServerFactoryCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public void createStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole() != UserRole.OWNER) {
            throw new ApplicationException(ErrorCode.NOT_OWNER);
        }

        validateTimes(storeRequestDto.getOpenTime(), storeRequestDto.getCloseTime());
        validateMinOrderPrice(storeRequestDto.getMinOrderPrice());
        if (hasExceededStoreLimit(user.getId())) {
            throw new ApplicationException(ErrorCode.STORE_MAX_OUT);
        }

        Store store = Store.createStore(
                storeRequestDto.getName(),
                storeRequestDto.getMinOrderPrice(),
                StoreStatus.OPEN,
                storeRequestDto.getOpenTime(),
                storeRequestDto.getCloseTime(),
                user
        );
        storeRepository.save(store);
    }

    public StoreResponseDto getStore(AuthUser authUser, Long id) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole() != UserRole.USER) {
            throw new ApplicationException(ErrorCode.NOT_USER);
        }

        Store store = storeRepository.findByIdAndRole(id, StoreStatus.OPEN);

        if (store == null) {
            throw new ApplicationException(ErrorCode.STORE_NOT_FOUND);
        }

        StoreResponseDto storeResponseDto = StoreResponseDto.of(store.getUser().getId(), store.getName(), store.getMinOrderPrice(), store.getOpenTime(), store.getCloseTime(), store.getMenuList());
        return storeResponseDto;
    }

    public List<StoreSimpleResponseDto> getStores(AuthUser authUser) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole() != UserRole.USER) {
            throw new ApplicationException(ErrorCode.NOT_USER);
        }
        List<Store> storeList = storeRepository.findAllByRole(StoreStatus.OPEN);
        if (storeList.isEmpty()) {
            throw new ApplicationException(ErrorCode.STORE_NOT_FOUND);
        }

        return storeList.stream().map(store -> StoreSimpleResponseDto.of(store.getId(), store.getName(), store.getMinOrderPrice(),
                store.getOpenTime(), store.getCloseTime())).collect(Collectors.toList());
    }

    @Transactional
    public void updateStore(AuthUser authUser, Long id, StoreRequestDto storeRequestDto) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Store store = storeRepository.findByIdAndRole(id, StoreStatus.OPEN);

        if ((user.getRole() != UserRole.OWNER) || (authUser.getId() != store.getUser().getId())) {
            throw new ApplicationException(ErrorCode.NOT_OWNER);
        }
        if (store.getRole() == StoreStatus.OUT) {
            throw new ApplicationException(ErrorCode.OUT_STORE);
        }
        validateTimes(storeRequestDto.getOpenTime(), storeRequestDto.getCloseTime());
        validateMinOrderPrice(storeRequestDto.getMinOrderPrice());


        store.update(storeRequestDto.getName(),
                storeRequestDto.getMinOrderPrice(),
                storeRequestDto.getOpenTime(),
                storeRequestDto.getCloseTime());

        storeRepository.save(store);
    }

    public void deleteStore(AuthUser authUser, Long id) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Store store = storeRepository.findByIdAndRole(id, StoreStatus.OPEN);
        if ((user.getRole() != UserRole.OWNER) || (authUser.getId() != store.getUser().getId())){
            throw new ApplicationException(ErrorCode.NOT_OWNER);
        }
        if (store == null) {
            throw new ApplicationException(ErrorCode.STORE_NOT_FOUND);
        }
        if(store.getRole() == StoreStatus.OUT)
        {
            throw new ApplicationException(ErrorCode.OUT_STORE);
        }
        try {
            store.changeStatus(StoreStatus.OUT);
            storeRepository.save(store);
        } catch (ApplicationException e) {
            throw new ApplicationException(ErrorCode.STORE_STATUS_CHANGE_FAILED);
        }
    }

    private void validateMinOrderPrice(int minOrderPrice) {
        if (minOrderPrice <= 0) {
            throw new ApplicationException(ErrorCode.INVAILD_MINORDERPRICE);
        }
    }

    private boolean hasExceededStoreLimit(Long userId) {
        List<Store> storeList = storeRepository.findAllByUserIdAndRole(userId, StoreStatus.OPEN);
        return storeList.size() >= StoreLimit.MAX_STORE_LIMIT;
    }

    public void validateTimes(LocalTime openTime, LocalTime closeTime) {
        if (openTime.equals(closeTime)) {
            throw new ApplicationException(ErrorCode.EQUALS_OPEN_CLOSE_TIME);
        }

    }


}

