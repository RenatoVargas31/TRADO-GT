package com.app.tradogt.repository;

import com.app.tradogt.entity.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {

    @Query("SELECT DISTINCT s FROM SubCategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :id")
    List<SubCategoria> findSubcategorias(@Param("id") int id);


}
