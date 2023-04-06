package com.dangol.dangolsonnimbackend.store.service.impl;

import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.file.service.FileService;
import com.dangol.dangolsonnimbackend.store.domain.Menu;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.MenuRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.MenuResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.MenuUpdateRequestDTO;
import com.dangol.dangolsonnimbackend.store.repository.MenuRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.MenuQueryRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuQueryRepository menuQueryRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final FileService fileService;

    public MenuServiceImpl(MenuRepository menuRepository, MenuQueryRepository menuQueryRepository, StoreQueryRepository storeQueryRepository, FileService fileService) {
        this.menuRepository = menuRepository;
        this.menuQueryRepository = menuQueryRepository;
        this.storeQueryRepository = storeQueryRepository;
        this.fileService = fileService;
    }

    @Override
    public MenuResponseDTO create(MenuRequestDTO dto) {
        Store store = storeQueryRepository.findById(dto.getStoreId()).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.STORE_NOT_FOUND)
        );
        String s3FileUrl = uploadFileIfPresent(dto.getMultipartFile());
        Menu menu = menuRepository.save(new Menu(dto.getName(), s3FileUrl, store, dto.getPrice()));
        store.getMenuList().add(menu);
        return new MenuResponseDTO(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuResponseDTO findById(Long menuId) {
        Menu menu = menuQueryRepository.findById(menuId).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.MENU_NOT_FOUND)
        );
        return new MenuResponseDTO(menu);
    }

    @Override
    public MenuResponseDTO update(MenuUpdateRequestDTO dto) {
        Menu menu = menuQueryRepository.findById(dto.getMenuId()).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.MENU_NOT_FOUND)
        );

        deleteExistingImageIfNewFilePresent(menu, dto.getMultipartFile());

        String s3FileUrl = uploadFileIfPresent(dto.getMultipartFile());
        menu.update(dto, s3FileUrl);
        return new MenuResponseDTO(menu);
    }

    @Override
    public void delete(Long menuId) {
        Menu menu = menuQueryRepository.findById(menuId).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.MENU_NOT_FOUND)
        );
        menu.getStore().getMenuList().remove(menu);
        menuRepository.deleteById(menuId);
    }

    private String uploadFileIfPresent(MultipartFile file) {
        if (file != null) {
            return fileService.upload(file);
        }
        return "";
    }

    private void deleteExistingImageIfNewFilePresent(Menu menu, MultipartFile newFile) {
        if (!menu.getImageUrl().equals("") && newFile != null) {
            fileService.fileDelete(menu.getImageUrl());
        }
    }
}
