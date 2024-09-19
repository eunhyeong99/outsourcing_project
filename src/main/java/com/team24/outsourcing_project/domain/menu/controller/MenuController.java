package com.team24.outsourcing_project.domain.menu.controller;

import com.team24.outsourcing_project.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
}
