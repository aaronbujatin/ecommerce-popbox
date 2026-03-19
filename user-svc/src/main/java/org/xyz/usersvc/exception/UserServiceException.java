package org.xyz.usersvc.exception;

import lombok.Getter;

@Getter
public class UserServiceException extends RuntimeException{

//    private final UserErrorInfo userErrorInfo;

    public UserServiceException(String message) {
        super(message);
    }

}
