package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ErasmusUserService {
    private final ErasmusUserRepository erasmusUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ErasmusUserService(ErasmusUserRepository erasmusUserRepository, PasswordEncoder passwordEncoder) {
        this.erasmusUserRepository = erasmusUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ErasmusUser findUserByIdAndRole(Long userId, UserRole role) {
        return erasmusUserRepository.findByIdAndRoles(userId, role);
    }

    public boolean isUserAdmin(Long userId) {
        ErasmusUser user = findUserByIdAndRole(userId, UserRole.ADMIN);
        return user != null;
    }

    public List<ErasmusUser> getAllErasmusUsers() {
        return erasmusUserRepository.findAll();
    }

    public ErasmusUser getErasmusUserByUsernameAndPassword(String username, String password) {
        ErasmusUser erasmusUser = erasmusUserRepository.findByUsername(username);
        if (erasmusUser != null && passwordEncoder.matches(password, erasmusUser.getPassword())) {
            return erasmusUser;
        }
        return null;
    }

    public ErasmusUser registerErasmusUser(ErasmusUser erasmusUser) {
        erasmusUser.setPassword(passwordEncoder.encode(erasmusUser.getPassword()));
        return erasmusUserRepository.save(erasmusUser);
    }

    public ErasmusUser getErasmusUserById(Long id) {
        return erasmusUserRepository.findById(id).orElse(null);
    }

    public ErasmusUser updateErasmusUser(Long id, ErasmusUser updatedUserData) {
        ErasmusUser existingUser = erasmusUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Erasmus user with ID " + id + " not found."));

        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setRoles(updatedUserData.getRoles());

        return erasmusUserRepository.save(existingUser);
    }

    public void deleteErasmusUser(Long id) {
        if (erasmusUserRepository.existsById(id)) {
            erasmusUserRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Erasmus user with ID " + id + " not found.");
        }
    }

    public ErasmusUser getErasmusUserByUsername(String username) {
        return erasmusUserRepository.findByUsername(username);
    }

    public void updateErasmusUser(ErasmusUser user) {
        erasmusUserRepository.save(user);
    }
}


