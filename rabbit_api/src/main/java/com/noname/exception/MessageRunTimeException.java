package com.noname.exception;

/**
 * @author ：liwuming
 * @date ：Created in 2021/5/25 13:37
 * @description ：
 * @modified By：
 * @version:
 */
public class MessageRunTimeException extends RuntimeException {

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message) {
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause) {
        super(cause);
    }
}
