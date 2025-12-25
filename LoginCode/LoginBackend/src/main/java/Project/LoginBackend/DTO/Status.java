package Project.LoginBackend.DTO;

public class Status {
    String message;
    String status;
    Status(){}
    public Status(String message, String status) {
        this.message = message;
        this.status = status;
    }
    public String getMessage(){
        return message;
    }
    public String getStatus(){
        return status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setStatus(String status){
        this.status =status;
    }
}
