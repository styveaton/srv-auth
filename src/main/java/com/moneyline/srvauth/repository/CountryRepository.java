package com.moneyline.srvauth.repository;

import com.moneyline.srvauth.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer> {

    Optional<Country> findByCode(String code);
}