package com.app.Controller;


import com.app.Exception.UserNotFound;
import com.app.Model.User;
import com.app.Service.AdministratorService;
import com.app.Service.BuyerService;
import com.app.Service.RetailerService;
import com.app.Service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * I have implemented a single controller to handle login, registration, and logout of any user type
 * This controller then uses instances of the admin, buyer, and retailer services to persist each user type in their respective repos.
 */
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true",
        maxAge = 3600)
@RestController
@RequestMapping
public class UserController {


    UserService userService;

    AdministratorService adminService;

    RetailerService retailerService;

    BuyerService buyerService;
    //MessageResponse messageResponse;

    public UserController(UserService userService, AdministratorService adminService,
                          RetailerService retailerService, BuyerService buyerService) {
        this.userService = userService;
        this.adminService = adminService;
        this.retailerService = retailerService;
        this.buyerService = buyerService;

    }


    /**
     * I have attempted to add additional code using authentication manager and jwtUtils to authenticate and create a secure Cookie
     * for login for each user
     */
    @PostMapping("login")
    public User login(@RequestBody User user) {
        try {
            return userService.login (user.getUsername(), user.getPassword());

        } catch (UserNotFound e) {
            System.out.println("invalid login credentials");
            return null;
        }
    }


    /**
     * This endpoint handles new "user" registration. Each user is assigned to a datatable based on the value in the
     * "role" parameter. The user is also added to a general datatable called "users"
     */
    @PostMapping("registration")
    public ResponseEntity addAccount(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {

        if(user.getRole().equals("buyer")){
            buyerService.addBuyer(user);
            userService.addAccount(user);
            return ResponseEntity.ok("Buyer successfully added");

        }else{
            retailerService.addAccount(user);
            userService.addAccount(user);
            return ResponseEntity.ok("Retailer successfully added");
        }


    }


    @PostMapping("logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "NewCookie")
                .body("You've been signed out!");
    }

    @PatchMapping("login/{id}")
    public User changePassword(@RequestBody User user, @PathVariable long id){
        return userService.changePassword(user, id);
    }


}
