package com.fiap.techchallenge.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "user_types",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}