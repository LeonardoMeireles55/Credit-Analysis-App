package com.leonardo.creditanalysisapp.mapper;

import com.leonardo.creditanalysisapp.domain.User;
import com.leonardo.creditanalysisapp.dto.UserDTO;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }

        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .cpf(entity.getCpf())
                .phoneNumber(entity.getPhoneNumber())
                .financialIncome(entity.getFinancialIncome())
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .cpf(dto.getCpf())
                .phoneNumber(dto.getPhoneNumber())
                .financialIncome(dto.getFinancialIncome())
                .build();
    }
}
