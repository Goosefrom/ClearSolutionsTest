package com.goose.clearsolutionstest.specification;

import com.goose.clearsolutionstest.dto.UserDTO;
import com.goose.clearsolutionstest.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {
    public static Specification<User> userSpecification(UserDTO userDTO) {
        return Specification.where(likeEmail(userDTO.getEmail()))
                .and(likeFirstName(userDTO.getFirstName()))
                .and(likeLastName(userDTO.getLastName()))
                .and(equalBirthDate(userDTO.getBirthDate()))
                .and(likeAddress(userDTO.getAddress()))
                .and(likePhoneNumber(userDTO.getPhoneNumber()));
    }

    private static Specification<User> likeEmail(String email) {
        if (email == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    private static Specification<User> likeFirstName(String firstName) {
        if (firstName == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
    }
    private static Specification<User> likeLastName(String lastName) {
        if (lastName == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
    }

    private static Specification<User> equalBirthDate(LocalDate birthDate) {
        if (birthDate == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("birthDate"), birthDate);
    }

    private static Specification<User> likeAddress(String address) {
        if (address == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("address"), "%" + address + "%");
    }

    private static Specification<User> likePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
    }

}
