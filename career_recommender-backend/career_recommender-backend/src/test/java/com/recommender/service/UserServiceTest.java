package com.recommender.service;


import com.recommender.exception.InvalidUserOrPasswordException;
import com.recommender.exception.UserAlreadyExistsException;
import com.recommender.model.User;
import com.recommender.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //activeaza suportul Mockito pentru a putea folosi adnotarile @Mock si @InjectMocks
public class UserServiceTest {

    //il folosim pentru a crea odublura a obiectului userRepository fara sa ne conectam la baza de date, doar simulam comportamentul ei
    @Mock
    private UserRepository userRepository;

    //creeaza o instanta reala a clasei UserService la care ii da dublura creata mai sus
    @InjectMocks
    private UserService userService;

    private User testUser;

    //aceasta metoda se executa automata inainte de fiecare test pentru a reseta datele
    @BeforeEach
    void setUp()
    {
        testUser = new User();
        testUser.setUsername("matei");
        testUser.setPassword("1234");

    }

    @Test
    void inregistrare_User()
    {
        //verificare daca utilizatorul existat inainte de a fi adaugat
        when(userRepository.existsByUsername("matei")).thenReturn(false);

        // Simulăm salvarea în baza de date
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        //apelam metoda de inregistrare
        User result = userService.registerUser(testUser);

        //verificam daca rezultatul este user-ul asteptat
        assertNotNull(result);
        assertEquals("matei", result.getUsername());
    }

    @Test
    void inregistrare_User_exceptie()
    {
        //utilizatorul exista deja
        when(userRepository.existsByUsername("matei")).thenReturn(true);

        //verificam daca se arunca exceptia specifica
        assertThrows(UserAlreadyExistsException.class, () ->{
            userService.registerUser((testUser));
        });

    }

    @Test
    void login_User()
    {
        //user-ul poate sa existe sau nu
        when(userRepository.findByUsername("matei")).thenReturn(Optional.of(testUser));

        //incercam sa ne inregistram cu datele corecte
        User result = userService.loginUser("matei", "1234");

        //verificam daca a functionat inregistrarea
        assertNotNull(result);
        assertEquals("matei", result.getUsername());

    }

    @Test
    void login_User_exceptie_parola()
    {
        //userul exista
        when(userRepository.findByUsername("matei")).thenReturn(Optional.of(testUser));

        //verificam daca se arunca exceptia cand parola e gresita
        assertThrows(InvalidUserOrPasswordException.class, () ->{
            userService.loginUser("matei","parola_gresita");
        });
    }

    @Test
    void login_User_exceptie_nume()
    {
        //userul exista
        when(userRepository.findByUsername("necunoscut")).thenReturn(Optional.empty());

        //verificam daca se arunca exceptia cand numele este gresit
        assertThrows(InvalidUserOrPasswordException.class, () ->{
            userService.loginUser("necunoscut","1234");
        });
    }


}
