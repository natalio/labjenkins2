package com.course.course.services.impl;

import com.course.course.models.CourseModel;
import com.course.course.models.LessonModel;
import com.course.course.models.ModuleModel;
import com.course.course.repositories.CourseRepository;
import com.course.course.repositories.LessonRepository;
import com.course.course.repositories.ModuleRepository;
import com.course.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    LessonRepository lessonRepository;


    /**
     * A anotação transação e importante para salvaguardar que não ha perda de dados em caso de falhas
     * **/
    @Transactional
    @Override
    public void delete(CourseModel courseModel) {


        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if(!moduleModelList.isEmpty()){
                for(ModuleModel module : moduleModelList){

                    List<LessonModel> lessonModelList =lessonRepository.findAllLessonIntoModule(module.getModuleId());
                    if (!lessonModelList.isEmpty()){
                        lessonRepository.deleteAll(lessonModelList);
                    }
                }
                moduleRepository.deleteAll(moduleModelList);
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
       return  courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public List<CourseModel> findALL() {
        return courseRepository.findAll();
    }
}
