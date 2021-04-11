package shradha.com.finalloginsignupauthproj.model;

public class User {
    public String userName;
    public String age;
    public String email;
    public int uId;

    public User() {

    }

    public User(String userName, String age, String email, int uId) {
        this.userName = userName;
        this.age = age;
        this.email = email;
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public int getuId() {
        return uId;
    }
}
