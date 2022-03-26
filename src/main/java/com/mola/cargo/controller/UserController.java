package com.mola.cargo.controller;

import com.mola.cargo.model.Role;
import com.mola.cargo.model.User;
import com.mola.cargo.service.RoleService;
import com.mola.cargo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/users")
    public String showUsers(Model model){
      model.addAttribute("users",userService.showUsers());
      model.addAttribute("roles", roleService.showRoles());
      return "securite/user";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "securite/login";
    }

    @GetMapping("/user/form")
    public String showFormAddUser(Model model){
       return "securite/formAddUser";
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
        //Set<Role> listerole = new HashSet<>();
        //listerole.add(role);
        //String pwdcrypter = bCryptPasswordEncoder.encode(password);
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setUsername(email);
        //user.setPassword(pwdcrypter);
        user.setRoles(roles);
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
