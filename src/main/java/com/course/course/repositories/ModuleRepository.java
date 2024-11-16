package com.course.course.repositories;

import com.course.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Query
 * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {


    /**
     *
     * Permite que neste metodo em especifico ele vai carregar tambem o objecto curso apsar deste estar definido como Lazy dentro do Model.
     * Com isso passamos a ter pesquisa dinamica/mista com comportamento do eager mesmo estando configurado com Type lazy
        @EntityGraph(attributePaths = {"course"})
        ModuleModel findbyTitle(String title);
    */

    /**@Modifying esta anotação e combinada com @Query para acção de update*/
    @Query(value = "select * from tb_modules where course_course_Id = :courseId",nativeQuery = true) /*** Query especifico */
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "select * from tb_modules where course_course_Id = :courseId and moduleId =:moduleId",nativeQuery = true) /*** Query especifico */
    Optional<ModuleModel> findModuleIntoCourse(@Param(value = "courseId") UUID courseId, @Param(value = "moduleId") UUID moduleId);
}
