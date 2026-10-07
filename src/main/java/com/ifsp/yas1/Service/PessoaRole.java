package com.ifsp.yas1.Service;

public enum PessoaRole {
    PROFESSOR("professor"),
    ALUNO("aluno");
    private String role;

    PessoaRole (String role){
        this.role = role;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}
