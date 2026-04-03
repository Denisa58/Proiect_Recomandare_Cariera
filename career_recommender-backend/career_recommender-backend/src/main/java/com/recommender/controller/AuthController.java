package com.recommender.controller;

import com.recommender.model.User;
import com.recommender.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.recommender.exception.UserAlreadyExistsException;
import com.recommender.exception.InvalidUserOrPasswordException;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Clasa pentru gestionarea proceselor de autentificare si inregistrare
 * Gestioneaza cererile HTTP venite din Frontend
 * Primeste cererile de la utilizatori pentru a crea conturi noi sau pentru a intra in conturile existente
 * Verifica daca datele sunt corecte si decide daca le permite accesul sau le trimite mesaj de eroare
 */


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        try{

            User registeredUser = userService.registerUser(user);

            registeredUser.setPassword(null);

            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

        } catch(UserAlreadyExistsException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginDetails){
        try {
            User user = userService.loginUser(loginDetails.getUsername(), loginDetails.getPassword());

            return new ResponseEntity<>(user.getId(), HttpStatus.OK);

        } catch (InvalidUserOrPasswordException ex){

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}