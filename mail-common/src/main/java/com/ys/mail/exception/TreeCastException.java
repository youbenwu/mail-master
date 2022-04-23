package com.ys.mail.exception;

/**
 * 自定义树结构抛异常
 * @author ghdhj
 */
public class TreeCastException extends RuntimeException{

    private static final long serialVersionUID = -7358633666514111106L;

    public TreeCastException(Throwable causer){
        super(causer);
    }
}
