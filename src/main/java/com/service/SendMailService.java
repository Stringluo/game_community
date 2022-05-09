package com.service;

public interface SendMailService {
    Boolean sendMail(String toMail, String vCode, String type);
}
