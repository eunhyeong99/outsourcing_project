package com.team24.outsourcing_project.domain.menu.controller;

import com.team24.outsourcing_project.domain.menu.dto.MenuRequestDto;
import com.team24.outsourcing_project.domain.menu.dto.MenuResponseDto;
import com.team24.outsourcing_project.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/menus")
    public String createMenus(@RequestBody MenuRequestDto menuRequestDto) {

        menuService.createMenus(menuRequestDto);

        return "생성 완료.";
    }

    @PutMapping("/menus/{id}")
    public String updateMenus(@RequestBody MenuRequestDto menuRequestDto, @PathVariable Long id) {

        menuService.updateMenus(menuRequestDto,id);

        return "수정 완료.";
    }

//    @GetMapping("/menus/{storeId}")
//    public List<MenuResponseDto> getMenus(@PathVariable(name = "storeId") Long storeId) {
//
//        // Fetch the menu list from the service
//        List<MenuResponseDto> menuResponseDtos = menuService.getMenus(storeId);
//
//        // Return the list of MenuResponseDto
//        return menuResponseDtos;
//    }

    @DeleteMapping("/menus/{id}")
    public String deleteMenus(@PathVariable Long id) {

        menuService.deleteMenus(id);

        return "삭제 완료.";
    }
}
