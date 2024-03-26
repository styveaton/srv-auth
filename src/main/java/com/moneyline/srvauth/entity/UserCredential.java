package com.moneyline.srvauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String fullname;
    private String langue;
    private String phone;
    private String accountReason;
    private String adressline;
    private String postalCode;
    private String country;
    private Integer countryId;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "accountNonExpired")
    private boolean accountNonExpired;
    @Column(name = "credentialsNonExpired")
    private boolean credentialsNonExpired;
    @Column(name = "accountNonLocked")
    private boolean accountNonLocked;
    private Date dateCreated;
    private Date dateUpdated;
    private Date dateLastConnection;
    private Date dateCodeGenerated;
    private String verificationCode;
    @Column(columnDefinition = "TEXT")
    private String token;
    private boolean userconnected;
}
