package com.ESN_Poliapp.Proiect;

public interface  AdminUserService {
    void registerAdminUser(AdminUser adminUser);
    AdminUser getAdminUserByUsernameAndPassword(String username, String password);
    AdminUser getAdminUserByUsername(String username);
    void updateAdminUser(AdminUser adminUser);
}

