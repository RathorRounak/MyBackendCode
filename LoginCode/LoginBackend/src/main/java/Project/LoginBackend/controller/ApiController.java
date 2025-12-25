package Project.LoginBackend.controller;

import Project.LoginBackend.DTO.Status;
import Project.LoginBackend.DTO.Store;
import Project.LoginBackend.DTO.UserInfo;
import Project.LoginBackend.service.LogicApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {
    public final LogicApi lp;
    public ApiController(LogicApi lp) {
        this.lp = lp;
    }

    @PostMapping("/register")
    public ResponseEntity<Status> register(@RequestBody Store store){

        Status s = lp.register(store);
        if("error".equals(s.getStatus())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(s);
    }

    @PostMapping("/login")
    public ResponseEntity<Status> login(@RequestBody Store store, HttpSession session ){
        Status s = lp.login(store,session);
        if ("error".equals(s.getStatus())) {
            return ResponseEntity.badRequest().body(s);
        }
        session.setAttribute("username",store.getUsername());
        return ResponseEntity.ok(s);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(401)
                    .body(new Status("error", "User not logged in"));
        }

        String user = (String) session.getAttribute("username");
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(new Status("error", "User not logged in"));
        }

        return ResponseEntity.ok(new UserInfo(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Status> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(
                new Status("success", "Logged out successfully")
        );
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<Status> deleteAccount(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
            if (session == null) {
                return ResponseEntity.status(401)
                        .body(new Status("error", "User not logged in"));
            }
            Status s = lp.deleteAccount(session);
            if("error".equals(s.getStatus())){
                return ResponseEntity.badRequest().body(s);
            }
            return ResponseEntity.ok(s);

    }

}
