package com.recommender.exception;

/**
 * Excepti aruncata atunci cand un utilizator incearca sa se inregistreze cu o adresa de email sau un username care exista deja in baza de date
 * Extinde RuntimeException pentru a putea fi aruncata in timpul executiei atunci cand user-ul sau parola sunt incorecte
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
