package com.laboratorio3.zoo_fantastico.service;
import java.lang.RuntimeException;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}