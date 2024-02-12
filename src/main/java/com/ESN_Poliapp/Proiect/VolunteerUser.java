package com.ESN_Poliapp.Proiect;

import jakarta.persistence.*;

@Entity
@Table(name = "volunteer_users")
public class VolunteerUser {

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

    // Alte atribute pentru utilizatorii voluntari
    // Adăugați aici alte atribute, dacă este cazul

    // Constructori

    public VolunteerUser() {
        // Constructor fără argumente
    }

    public VolunteerUser(String username, String email, String password, String firstName, String lastName, String nationality) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
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

    // Alte getteri și setteri pentru atributele suplimentare, dacă este cazul
}

