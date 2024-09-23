package com.team24.outsourcing_project.domain.user.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column()
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isDeleted = false;

    private User(final String email, final String password, final UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User create(final String email, final String password, final UserRole role) {
        return new User(email, password, role);
    }

    public void changeIsDeleted(boolean isDeleted) {
        this.isDeleted = true;
    }
}
