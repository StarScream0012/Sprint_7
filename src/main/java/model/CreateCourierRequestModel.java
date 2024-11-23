package model;

public class CreateCourierRequestModel {
      String login;
     String password;
     String firstName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public CreateCourierRequestModel(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
}
