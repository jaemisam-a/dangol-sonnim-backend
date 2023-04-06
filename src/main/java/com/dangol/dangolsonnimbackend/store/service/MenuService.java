package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.dto.MenuRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.MenuResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.MenuUpdateRequestDTO;

public interface MenuService {
    MenuResponseDTO create(MenuRequestDTO dto);

    MenuResponseDTO findById(Long menuId);

    MenuResponseDTO update(MenuUpdateRequestDTO dto);

    void delete(Long menuId);
}
