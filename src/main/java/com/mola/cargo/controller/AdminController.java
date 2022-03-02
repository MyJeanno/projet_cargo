package com.mola.cargo.controller;

import com.mola.cargo.model.Role;
import com.mola.cargo.model.User;
import com.mola.cargo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/users")
    public String showUsers(Model model){
      model.addAttribute("users",userService.showUsers());
      return "login/user";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login/login";
    }

    @GetMapping("/user/form")
    public String showFormAddUser(Model model){
       return "login/formAddUser";
    }

    @PostMapping("/user/nouveau")
    private String enregistrerUser(@RequestParam String nom,
                                   @RequestParam String prenom,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String roles){
        User user = new User();
        Role role = new Role();
        role.setNomRole(roles);
        Set<Role> listerole = new HashSet<>();
        listerole.add(role);
        String pwdcrypter = bCryptPasswordEncoder.encode(password);
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setUsername(email);
        user.setPassword(pwdcrypter);
        user.setRoles(listerole);
        userService.saveUser(user);
        return "redirect:/users";
    }

   /* @GetMapping("/logout")
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }*/
}
