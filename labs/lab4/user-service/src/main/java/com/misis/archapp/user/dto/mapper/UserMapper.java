package com.misis.archapp.user.dto.mapper;

import com.misis.archapp.user.db.User;
import com.misis.archapp.user.dto.UserCreateDTO;
import com.misis.archapp.user.dto.UserDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id")
    UserDTO toDTO(User user);

    User toEntity(UserCreateDTO userDTO);

    List<UserDTO> toDTOList(List<User> users);

}
