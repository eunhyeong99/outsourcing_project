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

        Menu menu = new Menu();

        menu.createMenu(
                menuRequestDto.getMenuName(),
                menuRequestDto.getMenuPrice()
        );

        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenus(MenuRequestDto menuRequestDto, Long id) {

        Menu menu = menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id를 찾을 수 없습니다."));

        menu.updateMenu(menuRequestDto.getMenuName(), menuRequestDto.getMenuPrice());

        menuRepository.save(menu);
    }

    public List<MenuResponseDto> getMenus(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("해당 id를 찾을 수 없습니다."));

        List<Menu> menus = store.getMenuList();

        return menus.stream()
                .map(menu -> new MenuResponseDto(menu.getMenuName(), menu.getMenuPrice()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMenus(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id를 찾을 수 없습니다."));
        menuRepository.delete(menu);
    }

}
