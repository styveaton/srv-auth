package com.moneyline.srvauth.repository;

import com.moneyline.srvauth.entity.RaisonCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RaisonCompteRepository extends JpaRepository<RaisonCompte,Integer> {

    Optional<RaisonCompte> findByCode(String code);
}
