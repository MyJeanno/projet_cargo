package com.mola.cargo.service;

import com.mola.cargo.model.Role;
import com.mola.cargo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> showRoles(){
        return roleRepository.findAll();
    }

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public Role showOneRole(Long id){
        Optional<Role> optional = roleRepository.findById(id);
        Role role = null;
        if(optional.isPresent()){
            role = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return role;
    }

    public void deleteRole(Long id){
        roleRepository.deleteById(id);
    }
}
