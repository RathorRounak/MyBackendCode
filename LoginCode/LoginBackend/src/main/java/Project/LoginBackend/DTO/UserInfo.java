package Project.LoginBackend.DTO;

public class UserInfo {
    String username;
    public UserInfo(){}
    public UserInfo(String userName){
        this.username = userName;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
}
