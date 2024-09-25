package com.team24.outsourcing_project.domain.menu.controller;

import com.team24.outsourcing_project.domain.menu.dto.MenuRequestDto;
import com.team24.outsourcing_project.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/menus")
    public void createMenus(@RequestBody MenuRequestDto menuRequestDto) {
        menuService.createMenus(menuRequestDto);
    }

    @PutMapping("/menus/{id}")
    public void updateMenus(@RequestBody MenuRequestDto menuRequestDto, @PathVariable Long id) {
        menuService.updateMenus(menuRequestDto,id);
    }

    @DeleteMapping("/menus/{id}")
    public void deleteMenus(@PathVariable Long id) {
        menuService.deleteMenus(id);
    }
}
