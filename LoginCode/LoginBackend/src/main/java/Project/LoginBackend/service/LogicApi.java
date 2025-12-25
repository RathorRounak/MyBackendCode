package Project.LoginBackend.service;

import Project.LoginBackend.DTO.Status;
import Project.LoginBackend.DTO.Store;
import Project.LoginBackend.DTO.UserInfo;
import Project.LoginBackend.entity.User;
import Project.LoginBackend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LogicApi {
    public final UserRepository ur;
    public LogicApi(UserRepository ur) {
        this.ur = ur;
    }

    public Status register(Store store){

        if (store.getPassword() == null || store.getUsername() == null) {
            return new Status("error","Password cannot be null");
        }

        if(store.getUsername().isEmpty() || store.getPassword().isEmpty()){
            return new Status("error","Username or Password cannot be empty");
        }
        if(store.getPassword().length() < 6 || store.getUsername().length() < 6){
            return new Status("error", "Username or Password too short");
        }
        if (ur.findByUsername(store.getUsername()).isPresent()) {
            return new Status("error", "Username already exists");
        }
        User user = new User(store.getUsername(),store.getPassword());
        ur.save(user);
        return new Status("success","User registered successfully");
    }

    public Status login(Store store, HttpSession session){
        if(store.getUsername()== null || store.getPassword()==null){
            return new Status("error", "Username or Password cannot be null");
        }
        if(store.getUsername().isEmpty() || store.getUsername().isEmpty()){
            return new Status("error","Username or Password cannot be empty");
        }
        return ur.findByUsername(store.getUsername())
                .map(user ->{
                        if(!user.getPassword().equals(store.getPassword())){
                        return new Status("error","Username or password is incorrect");
                        }
                        return new Status("success","logged in successfully");
                }).orElseGet(() ->
                        new Status("Error", "User not found")
                );

    }

    public UserInfo profile(HttpSession session){
        Object username = session.getAttribute("username");
        if(username==null){
            return null;
        }
        return new UserInfo((String) username);
    }
    @Transactional
    public Status deleteAccount(HttpSession session){
        if(session==null){
            return new Status("error","User not logged in");
        }
        String  username = (String) session.getAttribute("username");
        if(username == null){
            return new Status("error","User not logged in");
        }
        if(!ur.findByUsername(username).isPresent()){
            return new Status("error","User not found");
        }
        ur.deleteByUsername(username);
        session.invalidate();
        return new Status("success","Username deleted successfully deleted");
    }



}
