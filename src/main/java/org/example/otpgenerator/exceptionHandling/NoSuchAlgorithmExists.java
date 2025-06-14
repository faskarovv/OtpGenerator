package org.example.otpgenerator.exceptionHandling;


public class NoSuchAlgorithmExists extends RuntimeException{

    public NoSuchAlgorithmExists(String message){
        super(message);
    }
}
