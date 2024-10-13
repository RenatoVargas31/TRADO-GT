package com.app.tradogt.repository;

import com.app.tradogt.entity.Autenticacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutenticacionRepository extends JpaRepository<Autenticacion, Integer> {


}
