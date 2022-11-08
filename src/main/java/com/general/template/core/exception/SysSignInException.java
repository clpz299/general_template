package com.general.template.core.exception;

import com.general.template.auth.dto.SignInRequestDTO;
import org.springframework.security.core.AuthenticationException;

public class SysSignInException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    private SignInRequestDTO signInRequestDTO;

    /**
     * Construct a new instance of {@code AuthSignInException} with the given message.
     *
     * @param msg the message
     */
    public SysSignInException(SignInRequestDTO signInRequestDTO, String msg) {
        super(msg);
        this.signInRequestDTO = signInRequestDTO;
    }

    /**
     * Construct a new instance of {@code AuthSignInException} with the given message
     * and exception.
     *
     * @param msg the message
     * @param ex  the exception
     */
    public SysSignInException(SignInRequestDTO signInRequestDTO, String msg, Throwable ex) {
        super(msg, ex);
        this.signInRequestDTO = signInRequestDTO;
    }

    public SignInRequestDTO getSignInRequestDTO() {
        return signInRequestDTO;
    }
}
