package hr.mikrotron.remlog.controller;

public class NotAuthorizedException extends RuntimeException{
  public NotAuthorizedException() {
    super("User or device not authorized\n");
  }
}
