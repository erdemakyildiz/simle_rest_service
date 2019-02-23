package com.simple_rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Erdem Akyıldız on 22.02.2019.
 */
@RestController
public class UserController {

    private static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User("Erdem", "Akyıdlız"));
    }

    @GetMapping(path = "user")
    public List<User> getUserList() {
        return userList;
    }

    @GetMapping(path = "/auth")
    @ResponseBody
    public String checkAuth() {
        String jwtToken = Jwts.builder().setSubject("erdem").claim("roles", "user").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretKey").compact();

        return jwtToken;

    }

}
