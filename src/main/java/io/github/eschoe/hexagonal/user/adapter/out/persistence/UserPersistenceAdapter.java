package io.github.eschoe.hexagonal.user.adapter.out.persistence;

import io.github.eschoe.hexagonal.user.application.port.out.DeleteUserPort;
import io.github.eschoe.hexagonal.user.application.port.out.LoadUserPort;
import io.github.eschoe.hexagonal.user.application.port.out.SaveUserPort;
import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort, DeleteUserPort {

    private final UserMapper userMapper;

    public UserPersistenceAdapter(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userMapper.findByEmail(email.value()).map(UserEntity::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.from(user);
        if (entity.getId() == null) {
            userMapper.insert(entity);
        } else {
            userMapper.update(entity);
        }
        return entity.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }
}
