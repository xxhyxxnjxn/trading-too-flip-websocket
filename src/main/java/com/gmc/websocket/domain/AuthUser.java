package com.gmc.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "auth_user")
public class AuthUser {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 프로젝트에서 연결된 DB 의 넘버링 전략을 따라간다.
    @Column(nullable = false, name = "id")
    private Long id;
    @Column(nullable = false, name = "password")
    private String password;
    @Column(nullable = false, name = "last_login")
    private LocalDateTime lastLogin;
    @Column(nullable = true, name = "is_superuser")
    private boolean isSuperuser;
    @Column(nullable = false, name = "username")
    private String username;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = true, name = "is_staff")
    private boolean isStaff;
    @Column(nullable = false, name = "is_active")
    private boolean isActive;
    @Column(nullable = false, name = "date_joined")
    private LocalDateTime dateJoined;


}
