package com.wgplaner.core.entity;

import com.wgplaner.core.AuthServer;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_profile")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class UserProfile extends AbstractEntity {
    @Column(name = "name", unique = true)
    private String username;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "oid")
    private Long oid;
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_server")
    private AuthServer authServer;
}
