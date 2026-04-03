package com.recommender.exception;

/**
 * Exceptie pentru gestionar.ea erorilor de autentificare
 * Extinde RuntimeException pentru a putea fi aruncata in timpul executiei atunci cand user-ul sau parola sunt incorecte
 */
public class InvalidUserOrPasswordException extends RuntimeException {
    public InvalidUserOrPasswordException(String message) {
        super(message);
    }
}