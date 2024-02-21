package kfirmadoel.server_side.documents;

import org.springframework.data.annotation.Id;

public class users {

    @Id
    private String email;
    private String password;
    private String firstName;
    private String secondName;

    public users(String email, String password, String firstName, String secondName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "users [email=" + email + ", password=" + password + ", firstName=" + firstName + ", secondName="
                + secondName + "]";
    }

}
