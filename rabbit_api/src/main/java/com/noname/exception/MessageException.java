package com.noname.exception;

/**
 * @author ：liwuming
 * @date ：Created in 2021/5/25 13:33
 * @description ：
 * @modified By：
 * @version:
 */

public class MessageException extends Exception {

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
