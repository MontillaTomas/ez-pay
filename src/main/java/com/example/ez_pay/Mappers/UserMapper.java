
package com.example.ez_pay.Mappers;

import com.example.ez_pay.DTOs.UserDTO;
import com.example.ez_pay.Models.Role;
import com.example.ez_pay.Models.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(UserEntity user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setFirstname(user.getFirstname());
        dto.setLastName(user.getLastName());
        dto.setBirth(user.getBirth());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        if (user.getRol() != null) {
            String enumName = user.getRol().name();
            String dtoRol = enumName.substring(0, 1).toUpperCase() + enumName.substring(1).toLowerCase();
            dto.setRol(dtoRol);
        }

        return dto;
    }

    public UserEntity toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        UserEntity entity = new UserEntity();

        entity.setFirstname(userDTO.getFirstname());
        entity.setLastName(userDTO.getLastName());
        entity.setBirth(userDTO.getBirth());
        entity.setUsername(userDTO.getUsername());
        entity.setPassword(userDTO.getPassword());
        entity.setEmail(userDTO.getEmail());
        entity.setPhone(userDTO.getPhone());
        return entity;
    }
}
