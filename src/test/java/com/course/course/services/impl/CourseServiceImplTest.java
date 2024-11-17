package com.course.course.services.impl;

import com.course.course.enums.CourseLevel;
import com.course.course.enums.CourseStatus;
import com.course.course.models.CourseModel;
import com.course.course.models.LessonModel;
import com.course.course.models.ModuleModel;
import com.course.course.repositories.CourseRepository;
import com.course.course.repositories.LessonRepository;
import com.course.course.repositories.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    CourseServiceImpl courseService;

    @Mock
    CourseRepository courseRepository;

    @Mock
    ModuleRepository moduleRepository;

    @Mock
    LessonRepository lessonRepository;

    @Test
    void test_save_success(){
        //arrange
        CourseModel courseModel = new CourseModel();
        courseModel.setCourseId(UUID.randomUUID());
        courseModel.setCourseLevel(CourseLevel.ADVANCED);
        courseModel.setCourseStatus(CourseStatus.INPROGRESS);
        courseModel.setName("Curso 2");
        courseModel.setDescription("Aprenda Spring para aplicações");
        courseModel.setUserInstructor(UUID.randomUUID());
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));


        when(courseRepository.save(courseModel)).thenReturn(courseModel);

        //action
        CourseModel sut = courseService.save(courseModel);

        //assert
        assertEquals(sut.getCourseId(), courseModel.getCourseId());
    }

    @Test
    void test_save_exeption(){
        //arrange
        CourseModel courseModel = new CourseModel();
        courseModel.setCourseId(UUID.randomUUID());
        courseModel.setCourseLevel(CourseLevel.ADVANCED);
        courseModel.setCourseStatus(CourseStatus.INPROGRESS);
        courseModel.setName("Curso 2");
        courseModel.setDescription("Aprenda Spring para aplicações");
        courseModel.setUserInstructor(UUID.randomUUID());
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        when(courseRepository.save(courseModel)).thenThrow(RuntimeException.class);

        //action//assert
        assertThrows(RuntimeException.class, ()->courseService.save(courseModel));
        verify(courseRepository,times(1)).save(courseModel);

    }

    @Test
    void test_delete_curso_success(){
        //arrange
        CourseModel courseModel = new CourseModel();
        courseModel.setCourseId(UUID.randomUUID());
        courseModel.setCourseLevel(CourseLevel.ADVANCED);
        courseModel.setCourseStatus(CourseStatus.INPROGRESS);
        courseModel.setName("Curso 2");
        courseModel.setDescription("Aprenda Spring para aplicações");
        courseModel.setUserInstructor(UUID.randomUUID());
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        List<ModuleModel> moduleModelList = List.of();

        when(moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId())).thenReturn(moduleModelList);
        doNothing().when(courseRepository).delete(courseModel);//para metodos voids

        //action
        courseService.delete(courseModel);

        //assert
        verify(courseRepository,times(1)).delete(courseModel);

    }

    @Test
    void test_delete_mudule_curso_without_lesson_success(){
        //arrange
        CourseModel courseModel = new CourseModel();
        courseModel.setCourseId(UUID.randomUUID());
        courseModel.setCourseLevel(CourseLevel.ADVANCED);
        courseModel.setCourseStatus(CourseStatus.INPROGRESS);
        courseModel.setName("Curso 2");
        courseModel.setDescription("Aprenda Spring para aplicações");
        courseModel.setUserInstructor(UUID.randomUUID());
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        ModuleModel moduleModel = new ModuleModel();
        moduleModel.setCourse(courseModel);
        moduleModel.setDescription("qualquer");
        moduleModel.setTitle("teste");
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        List<ModuleModel> moduleModelList = List.of(moduleModel);

        List<LessonModel> lessonModelList = List.of();

        when(lessonRepository.findAllLessonIntoModule(any())).thenReturn(lessonModelList);
        when(moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId())).thenReturn(moduleModelList);
        doNothing().when(moduleRepository).deleteAll(any());//para metodos voids
        doNothing().when(courseRepository).delete(courseModel);//para metodos voids

        //action
        courseService.delete(courseModel);

        //assert
        verify(courseRepository,times(1)).delete(courseModel);
        verify(moduleRepository,times(1)).deleteAll(any());
        verify(lessonRepository,times(0)).deleteAll(any());

    }

    @Test
    void test_delete_mudule_curso_lesson_success(){
        //arrange
        CourseModel courseModel = new CourseModel();
        courseModel.setCourseId(UUID.randomUUID());
        courseModel.setCourseLevel(CourseLevel.ADVANCED);
        courseModel.setCourseStatus(CourseStatus.INPROGRESS);
        courseModel.setName("Curso 2");
        courseModel.setDescription("Aprenda Spring para aplicações");
        courseModel.setUserInstructor(UUID.randomUUID());
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        ModuleModel moduleModel = new ModuleModel();
        moduleModel.setCourse(courseModel);
        moduleModel.setDescription("qualquer");
        moduleModel.setTitle("teste");
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        LessonModel lessonModel = new LessonModel();
        lessonModel.setLessonId(UUID.randomUUID());
        lessonModel.setDescription("qualquer");
        lessonModel.setTitle("teste");
        lessonModel.setModule(moduleModel);

        List<ModuleModel> moduleModelList = List.of(moduleModel);

        List<LessonModel> lessonModelList = List.of(lessonModel);

        when(lessonRepository.findAllLessonIntoModule(any())).thenReturn(lessonModelList);
        when(moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId())).thenReturn(moduleModelList);
        doNothing().when(moduleRepository).deleteAll(any());//para metodos voids
        doNothing().when(courseRepository).delete(courseModel);//para metodos voids

        //action
        courseService.delete(courseModel);

        //assert
        verify(courseRepository,times(1)).delete(courseModel);
        verify(moduleRepository,times(1)).deleteAll(any());
        verify(lessonRepository,times(1)).deleteAll(any());

    }

}