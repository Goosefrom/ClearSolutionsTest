package com.goose.clearsolutionstest.service;

import com.goose.clearsolutionstest.dto.UpdateUserDTO;
import com.goose.clearsolutionstest.dto.UserDTO;
import com.goose.clearsolutionstest.exception.ErrorType;
import com.goose.clearsolutionstest.exception.ProjectException;
import com.goose.clearsolutionstest.mapper.UserMapper;
import com.goose.clearsolutionstest.model.User;
import com.goose.clearsolutionstest.repository.UserRepository;
import com.goose.clearsolutionstest.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService{

    private final Environment env;
    private final UserRepository repository;
    private final UserMapper mapper;

    private boolean validateDate(LocalDate date) throws NullPointerException{
        return date.isBefore(LocalDate.now().minusYears(env.getProperty("user.minimum.age", Integer.class)));
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        if (!validateDate(userDTO.getBirthDate()))
            throw new ProjectException(ErrorType.INTERNAL_ERROR, "You should be older than eighteen");
        if (repository.exists(UserSpecification.userSpecification(userDTO)))
            throw new ProjectException(ErrorType.ALREADY_OCCUPIED, "This user is already exist");
        User save = repository.save(mapper.dtoToModel(userDTO));
        return mapper.modelToDTO(save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAllByDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to.minusDays(1)))
            throw new ProjectException(ErrorType.INTERNAL_ERROR,
                    "First date should be earlier than second");
        List<User> results = repository.findAllByBirthDateBetween(from, to);
        return results.stream().map(mapper::modelToDTO).toList();
    }

    @Override
    public UserDTO update(Long id, UpdateUserDTO updateUserDTO) {
        User updateUser = repository.findById(id)
                .orElseThrow(() -> new ProjectException(ErrorType.NOT_FOUND, "User not found"));
        mapper.updateUser(updateUser, updateUserDTO);
        if (!validateDate(updateUser.getBirthDate()))
            throw new ProjectException(ErrorType.INTERNAL_ERROR, "You should be older than eighteen");
        return mapper.modelToDTO(updateUser);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ProjectException(ErrorType.NOT_FOUND, "User not found");
        repository.deleteById(id);
    }

}
