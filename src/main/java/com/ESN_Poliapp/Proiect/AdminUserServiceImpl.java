package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;

    @Autowired
    public AdminUserServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public void registerAdminUser(AdminUser adminUser) {
        // Implementare înregistrare utilizator admin în baza de date
        adminUserRepository.save(adminUser);
    }

    @Override
    public AdminUser getAdminUserByUsernameAndPassword(String username, String password) {
        // Implementare obținere utilizator admin după nume de utilizator și parolă
        return adminUserRepository.findByUsername(username);
    }

    @Override
    public AdminUser getAdminUserByUsername(String username) {
        // Implementare obținere utilizator admin după nume de utilizator
        return adminUserRepository.findByUsername(username);
    }

    @Override
    public void updateAdminUser(AdminUser adminUser) {
        // Implementare actualizare utilizator admin în baza de date
        adminUserRepository.save(adminUser);
    }
}

