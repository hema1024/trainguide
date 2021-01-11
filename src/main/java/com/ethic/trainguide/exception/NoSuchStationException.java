package com.ethic.trainguide.exception;

public class NoSuchStationException extends Exception {

    public NoSuchStationException(Exception e) {
        super(e);
    }

    public NoSuchStationException(String msg) {
        super(msg);
    }

}
