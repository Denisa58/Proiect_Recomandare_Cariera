package com.recommender.service;

import com.recommender.exception.UserAlreadyExistsException;
import com.recommender.exception.InvalidUserOrPasswordException;
import com.recommender.model.User;
import com.recommender.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Clasa care gestioneaza autentficarea si inregistrarea utilizatorului
 * Verifica daca numele de utilizator este disponibil la inregistrare si valideaza datele de logare atunci cand
 * cineva vrea sa intre in cont
 * Face legatura intre formularul de login si baza de date cu utilizatori
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username-ul este deja folosit.");
        }

        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidUserOrPasswordException("Username sau parolă incorectă."));

        if (user.getPassword().equals(password)) {
            return user;
        }

        throw new InvalidUserOrPasswordException("Username sau parolă incorectă.") ;
    }
}