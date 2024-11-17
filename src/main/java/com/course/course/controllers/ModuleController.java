package com.course.course.controllers;

import com.course.course.dto.ModuleDto;
import com.course.course.models.CourseModel;
import com.course.course.models.ModuleModel;
import com.course.course.services.CourseService;
import com.course.course.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;
    @Autowired
    CourseService courseService;

    @PostMapping("/courses/{courseId}/module")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId")UUID courseId, @RequestBody @Valid ModuleDto moduleDto){
        Optional<CourseModel> courseModelOptional= courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found. USER DANI Brito");
        }

        var moduleModel= new ModuleModel();
        BeanUtils.copyProperties(moduleDto,moduleModel);
        moduleModel.setCourse(courseModelOptional.get());
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/courses/{courseId}/module/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> moduleModelOptional= moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course or Module Not Found.");
        }
        moduleService.delete(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successful.");
    }

    @PutMapping("/courses/{courseId}/module/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId, @RequestBody @Valid  ModuleDto moduleDto){
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course or Module not found");
        }
        var moduleModel= moduleModelOptional.get();
        BeanUtils.copyProperties(moduleDto,moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }


}
