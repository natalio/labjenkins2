package com.course.course.repositories;

import com.course.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query(value = "select *  from tb_lessons where module_moduleId = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonIntoModule(@Param("moduleId") UUID moduleId);
}
