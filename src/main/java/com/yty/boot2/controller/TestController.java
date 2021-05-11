package com.yty.boot2.controller;

import com.yty.boot2.domain.entity.TestEntity;
import com.yty.boot2.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by yangtianyu.
 */
@RestController
@RequestMapping(path = "/test")
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping(path = "/detail")
    public TestEntity detail(Long id) {
        return testService.findById(id);
    }

    @PostMapping(path = "/save")
    public void save(@RequestBody TestEntity entity) {
        testService.create(entity);
    }

    @PostMapping(value = "/upload")
    public String upload(UploadRequest request) {
        return request.getFile().getOriginalFilename();
    }

    public static class UploadRequest {
        public MultipartFile getFile() {
            return file;
        }

        public UploadRequest setFile(MultipartFile file) {
            this.file = file;
            return this;
        }

        private MultipartFile file;
    }
}
