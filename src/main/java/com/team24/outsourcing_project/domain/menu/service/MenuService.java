package com.team24.outsourcing_project.domain.menu.service;

import com.team24.outsourcing_project.domain.menu.dto.MenuRequestDto;
import com.team24.outsourcing_project.domain.menu.dto.MenuResponseDto;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.menu.repository.MenuRepository;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createMenus(MenuRequestDto menuRequestDto) {
        Store store = storeRepository.findById(menuRequestDto.getStoreId()).orElse(null);
        Menu menu = new Menu();

        menu.createMenu(
                store,
                menuRequestDto.getMenuName(),
                menuRequestDto.getMenuPrice()

        );
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenus(MenuRequestDto menuRequestDto, Long id) {
        Store store = storeRepository.findById(menuRequestDto.getStoreId()).orElse(null);
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 찾을 수 없습니다."));
        menu.updateMenu(menuRequestDto.getMenuName(), menuRequestDto.getMenuPrice());
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenus(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 찾을 수 없습니다."));
        menuRepository.delete(menu);

    }

}
