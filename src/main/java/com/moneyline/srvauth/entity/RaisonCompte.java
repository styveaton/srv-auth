package com.moneyline.srvauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tj_reason_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaisonCompte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String description;
}
