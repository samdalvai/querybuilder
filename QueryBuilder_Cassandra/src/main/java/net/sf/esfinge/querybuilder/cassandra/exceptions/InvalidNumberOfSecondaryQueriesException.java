package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class InvalidNumberOfSecondaryQueriesException extends RuntimeException{
    public InvalidNumberOfSecondaryQueriesException(String message) {
        super(message);
    }
}
