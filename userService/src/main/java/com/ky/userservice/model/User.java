package com.ky.userservice.model;

import com.ky.userservice.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @Column(length = 1000)
    private String profileImage;
    private Date lastLogin;
    private Date lastLoginShow;
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
    private boolean isEnabled;
    private boolean isNotLocked;

    // Establishing OneToOne relationship with File entity for profile image
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
    private File profileImg;

}
