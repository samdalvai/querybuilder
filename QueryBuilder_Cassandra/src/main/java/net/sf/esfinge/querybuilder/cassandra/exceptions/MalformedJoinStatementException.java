package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class MalformedJoinStatementException extends RuntimeException{
    public MalformedJoinStatementException(String message) {
        super(message);
    }
}
