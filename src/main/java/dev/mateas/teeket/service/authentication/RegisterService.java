package dev.mateas.teeket.service.authentication;

import dev.mateas.teeket.dto.authentication.RegisterDto;
import dev.mateas.teeket.entity.authentication.User;
import dev.mateas.teeket.exception.authentication.EmailAlreadyExistsException;
import dev.mateas.teeket.exception.authentication.PasswordDoesNotSatisfyConstraintsException;
import dev.mateas.teeket.exception.authentication.PasswordsDoNotMatchException;
import dev.mateas.teeket.repository.UserRepository;
import dev.mateas.teeket.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto) throws EmailAlreadyExistsException, PasswordsDoNotMatchException, PasswordDoesNotSatisfyConstraintsException {
        if (userRepository.findByUsername(registerDto.getUsername()).size() > 0) {
            throw new EmailAlreadyExistsException();
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        if (!passwordValidator.validate(registerDto.getPassword())) {
            throw new PasswordDoesNotSatisfyConstraintsException();
        }

        User user = new User(registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()));
        userRepository.insert(user);
    }
}
