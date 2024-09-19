package com.team24.outsourcing_project.domain.store.controller;

import com.team24.outsourcing_project.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
}
