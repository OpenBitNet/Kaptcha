package com.mazepeng.kaptcha.exception;

/**
 * 验证码生成时发生的异常
 */
public class CaptchaGenerationException extends RuntimeException {

    public CaptchaGenerationException(String message) {
        super(message);
    }

    public CaptchaGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}