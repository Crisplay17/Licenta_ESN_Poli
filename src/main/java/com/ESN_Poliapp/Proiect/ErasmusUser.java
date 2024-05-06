package com.ESN_Poliapp.Proiect;

import jakarta.persistence.*;

@Entity
@Table(name = "erasmus_users")
public class ErasmusUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String nationality;

    @Column(name = "host_university")
    private String hostUniversity;

    @Column(name = "receiving_university")
    private String receivingUniversity;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    // Constructori

    public ErasmusUser() {
        // Constructor fără argumente
    }

    public ErasmusUser(String username, String email, String password, String firstName, String lastName, String nationality, String hostUniversity, String receivingUniversity) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.hostUniversity = hostUniversity;
        this.receivingUniversity = receivingUniversity;
    }

    // Getteri și setteri pentru toate atributele

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHostUniversity() {
        return hostUniversity;
    }

    public void setHostUniversity(String hostUniversity) {
        this.hostUniversity = hostUniversity;
    }

    public String getReceivingUniversity() {
        return receivingUniversity;
    }

    public void setReceivingUniversity(String receivingUniversity) {
        this.receivingUniversity = receivingUniversity;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}


