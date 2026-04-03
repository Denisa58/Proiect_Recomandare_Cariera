package com.recommender.model;

import jakarta.persistence.*;


/**
 * Clasa care descrie un  utilizator
 * Pastreaza datele esentiale pentru contul fiecarei persoane cum ar fi numele de utilizator, adresa de email si parola
 */
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //id-ul se incrementeaza automat in baza de date
    private Long id;

    @Column(name="username", unique=true, length=50, nullable=false)
    private String username;

    @Column(name="email", unique=true, length=100, nullable=false)
    private String email;

    @Column(name="password", nullable=false, length=255)
    private String password;

    public User(){
    }

    public User(String u, String e, String p){
        this.username = u;
        this.email = e;
        this.password = p;
    }

    public Long getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }


    public void setId(Long i){
        this.id = i;
    }
    public void setUsername(String u){
        this.username = u;
    }
    public void setEmail(String e){
        this.email = e;
    }
    public void setPassword(String p){
        this.password = p;
    }

}