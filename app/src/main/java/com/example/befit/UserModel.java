package com.example.befit;

import io.realm.RealmObject;

public class UserModel extends RealmObject {
    private String name;
    private String apellido;
    private String telefono;
    private String email;

    public UserModel(String name, String apellido, String telefono, String email) {
        this.name = name;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;

    }
    public UserModel() {}


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

