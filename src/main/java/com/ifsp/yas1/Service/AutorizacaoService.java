package com.ifsp.yas1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifsp.yas1.Repository.PessoaRepository;

@Service
public class AutorizacaoService implements UserDetailsService{

    @Autowired
    PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return pessoaRepository.findByUsername(username);
    }
}
