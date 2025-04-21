package com.zarubovandlevchenko.lb1.model.dbuser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String login;

    private String password;

    private String firstName;

    private String lastName;


    @Column(unique = true)
    private String passportNumber;

}