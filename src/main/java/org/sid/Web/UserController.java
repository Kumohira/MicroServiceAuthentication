package org.sid.Web;

import lombok.Data;
import org.sid.Entities.AppUser;
import org.sid.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final AccountService accountService;

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody UserForm userForm) {
        return accountService.saveUser(userForm.getUsername(), userForm.getPassword(), userForm.getConfirmedPassword());
    }
}

@Data
class UserForm {
    private String username;
    private String password;
    private String confirmedPassword;
}
