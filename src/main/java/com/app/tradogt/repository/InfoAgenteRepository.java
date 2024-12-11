package com.app.tradogt.repository;

import com.app.tradogt.entity.InfoAgente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoAgenteRepository extends JpaRepository<InfoAgente, String> {
}
