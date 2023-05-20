package com.group4.Inmobiliaria.service;

import com.group4.Inmobiliaria.entidades.Cliente;
import com.group4.Inmobiliaria.entidades.Ente;
import com.group4.Inmobiliaria.entidades.UserEntity;
import com.group4.Inmobiliaria.enums.Rol;
import com.group4.Inmobiliaria.repository.ClienteRepository;
import com.group4.Inmobiliaria.repository.EnteRepository;
import com.group4.Inmobiliaria.utils.Session;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EnteRepository enteRepository;

    @Transactional
    public void registrarCliente(Cliente cliente) {

        cliente.setRol(Rol.CLIENTE);
        
        cliente.setPassword(new BCryptPasswordEncoder().encode(cliente.getPassword()));

        clienteRepository.save(cliente);
    }

    @Transactional
    public void registrarEnte(Ente ente) {

        ente.setRol(Rol.ENTE);
        
        ente.setPassword(new BCryptPasswordEncoder().encode(ente.getPassword()));

        enteRepository.save(ente);

    }

    private UserEntity findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente != null) {
            return cliente;
        }

        Ente ente = enteRepository.findByEmail(email);
        if (ente != null) {
            return ente;
        }

        return null;
    }

    private User user(UserEntity user) {
        List<GrantedAuthority> permisos = new ArrayList();
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol());
        permisos.add(p);
        Session.setUserSession(user);
        return new User(user.getEmail(), user.getPassword(), permisos);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = findByEmail(email);

        if (userEntity != null) {

            if (userEntity instanceof Ente) {
                Ente ente = (Ente) userEntity;
                return user(ente);
            }

            if (userEntity instanceof Cliente) {
                Cliente cliente = (Cliente) userEntity;
                return user(cliente);
            }
        }
        return null;
    }

}
