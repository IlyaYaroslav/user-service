package com.taskspace.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 200)
    private String email;

    @Column(name = "role", nullable = false, length = 50)
    private String role;
}