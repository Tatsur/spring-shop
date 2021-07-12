package com.ttsr.springshop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;
    private String password;
    private String login;
    private String lastName;
    private String secondName;
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> role;

    public String getFIO() {
        return String.format("%s %s %s",
                getLastName() != null ? getLastName() : "",
                getName() != null ? getName() : "",
                getSecondName() != null ? getSecondName() : "");
    }
}
