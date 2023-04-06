package com.dangol.dangolsonnimbackend.store.controller;

import com.dangol.dangolsonnimbackend.store.dto.MenuRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.MenuResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.MenuUpdateRequestDTO;
import com.dangol.dangolsonnimbackend.store.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/create")
    public ResponseEntity<MenuResponseDTO> create(@Valid @ModelAttribute MenuRequestDTO dto) {
        MenuResponseDTO res = menuService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @GetMapping("/find/{menuId}")
    public ResponseEntity<MenuResponseDTO> findById(@PathVariable Long menuId){
        MenuResponseDTO res = menuService.findById(menuId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PostMapping("/update")
    public ResponseEntity<MenuResponseDTO> update(@Valid @ModelAttribute MenuUpdateRequestDTO dto){
        MenuResponseDTO res = menuService.update(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @DeleteMapping("/delete/{menuId}")
    public ResponseEntity<Void> delete(@PathVariable Long menuId){
        menuService.delete(menuId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
