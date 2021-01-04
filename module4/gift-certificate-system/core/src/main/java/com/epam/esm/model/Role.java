package com.epam.esm.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.MERGE})
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Set<User> users;

}
