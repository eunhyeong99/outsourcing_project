package com.team24.outsourcing_project.domain.store.service;


import com.team24.outsourcing_project.domain.common.dto.AuthUser;
import com.team24.outsourcing_project.domain.store.dto.StoreRequestDto;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.entity.StoreStatus;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRole;
import com.team24.outsourcing_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private StoreService storeService;

    @Test
    public void 스토어_생성_정상동작된다() {
        AuthUser authUser = new AuthUser(1L, "test123@test.com", UserRole.OWNER);
        User user = User.create(authUser.getEmail(), "test123!", authUser.getRole());
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // StoreRequestDto 객체 생성
        StoreRequestDto storeRequestDto = new StoreRequestDto();
        ReflectionTestUtils.setField(storeRequestDto, "name", "가게");
        ReflectionTestUtils.setField(storeRequestDto, "minOrderPrice", 100);
        ReflectionTestUtils.setField(storeRequestDto, "openTime", LocalTime.parse("15:00"));
        ReflectionTestUtils.setField(storeRequestDto, "closeTime", LocalTime.parse("16:00"));

        Store expectedStore = Store.createStore(
                storeRequestDto.getName(),
                storeRequestDto.getMinOrderPrice(),
                StoreStatus.OPEN,
                storeRequestDto.getOpenTime(),
                storeRequestDto.getCloseTime(),
                user
        );

        // Stubbing
        given(storeRepository.save(any(Store.class))).willReturn(expectedStore);

        storeService.createStore(authUser, storeRequestDto);

        // Then: 검증
        verify(storeRepository).save(any(Store.class));
    }

//    @Test
//    public void 스토어_조회를_성공한다() {
//        // AuthUser 및 User 객체 설정
//        AuthUser authUser = new AuthUser(1L, "test123@test.com", UserRole.USER);
//        User foundUser = User.create(authUser.getEmail(), "test123!", authUser.getRole());
//        ReflectionTestUtils.setField(foundUser, "id", authUser.getId());
//
//        given(userRepository.findById(anyLong())).willReturn(Optional.of(foundUser));
//
//        // Store 객체 설정
//        Store foundStore = Store.createStore("스토어",
//                15000,
//                StoreStatus.OPEN,
//                LocalTime.parse("17:00"),
//                LocalTime.parse("23:00"),
//                foundUser);
//        ReflectionTestUtils.setField(foundStore, "id", 1L);
//        given(storeRepository.findById(1L)).willReturn(Optional.of(foundStore));
//        // Menu 객체 생성 및 ID 설정
//        List<Menu> menuList = new ArrayList<>();
//        Menu menu1 = new Menu();
//        ReflectionTestUtils.setField(menu1, "id", 1L); // ID 설정 후 추가
//        menu1.createMenu(foundStore, "치킨", 15000);
//        menuList.add(menu1);
//
//        Menu menu2 = new Menu();
//        ReflectionTestUtils.setField(menu2, "id", 2L); // ID 설정 후 추가
//        menu2.createMenu(foundStore, "피자", 25000);
//        menuList.add(menu2);
//
//        // Store 객체에 메뉴 목록 설정
//        ReflectionTestUtils.setField(foundStore, "menuList", menuList);
//
//        StoreResponseDto actualResponseDto = storeService.getStore(authUser, 1L);
//
//        // then
//        assertEquals(1L,actualResponseDto.getUserId(),"User ID should match");
//        assertEquals("스토어",actualResponseDto.getName(),"Name should match");
//        assertEquals(15000,actualResponseDto.getMinOrderPrice(),"MinOrderPrice should match");
//        assertEquals(LocalTime.parse("17:00"),actualResponseDto.getOpenTime(),"OpenTime should match");
//        assertEquals(LocalTime.parse("23:00"),actualResponseDto.getCloseTime(),"CloseTime should match");
//        assertEquals(menuList,actualResponseDto.getMenuList(),"MenuList should match");
//
//    }
}









