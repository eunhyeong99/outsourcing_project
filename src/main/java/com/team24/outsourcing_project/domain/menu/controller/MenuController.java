package com.team24.outsourcing_project.domain.menu.controller;

import com.team24.outsourcing_project.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
}
