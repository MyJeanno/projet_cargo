package com.mola.cargo.controller;

import com.mola.cargo.model.Role;
import com.mola.cargo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public String showRoles(Model model){
        model.addAttribute("roles", roleService.showRoles());
        return "securite/role";
    }

    @PostMapping("/role/nouveau")
    public String enregistrerRole(@RequestParam String nom){
        Role role = new Role();
        role.setNomRole(nom);
        roleService.saveRole(role);
        return "redirect:/roles";
    }

    @GetMapping("role/formUpdate/{id}")
    public String formUpdateRole(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRole", roleService.showOneRole(id));
        return "securite/formEditRole";
    }

    @GetMapping("/role/update")
    public String updateRole(@ModelAttribute("role") Role role){
        roleService.saveRole(role);
        return "redirect:/roles";
    }

    @GetMapping("role/delete")
    public String supprimerRole(Long id){
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
