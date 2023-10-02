package com.goose.clearsolutionstest.controller;

import com.goose.clearsolutionstest.dto.UpdateUserDTO;
import com.goose.clearsolutionstest.dto.UserDTO;
import com.goose.clearsolutionstest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDTO create(@Valid @RequestBody UserDTO userDTO) {
        UserDTO data = service.create(userDTO);
        return data;
    }

    @GetMapping
    public List<UserDTO> findAllByDateRange(@RequestParam(required = false) LocalDate from,
                                            @RequestParam(required = false) LocalDate to) {
        List<UserDTO> data = service.findAllByDateRange(from, to);
        return data;
    }

    @PatchMapping(path = "/{id}")
    public UserDTO update(@PathVariable Long id,
                          @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        UserDTO data = service.update(id, updateUserDTO);
        return data;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("ok");
    }
}
