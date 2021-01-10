package com.ethic.trainguide.exception;

public class NoSuchRouteException extends Exception {

    public NoSuchRouteException(Exception e) {
        super(e);
    }

    public NoSuchRouteException(String msg) {
        super(msg);
    }

}
