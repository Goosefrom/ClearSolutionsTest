package com.goose.clearsolutionstest.service;

import com.goose.clearsolutionstest.dto.UpdateUserDTO;
import com.goose.clearsolutionstest.dto.UserDTO;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserDTO create(UserDTO userDTO);
    List<UserDTO> findAllByDateRange(LocalDate from, LocalDate to);
    UserDTO update(Long id, UpdateUserDTO updateUserDTO);
    void delete(Long id);
}
