package org.tricol.supplierchain.exception;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(){
        super("Anonymous, Please authenticate to access the required resource");
    }
}
