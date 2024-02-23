package info.rue.examples.controller;

import info.rue.examples.service.Exam02Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples/exam02")
@Slf4j
@RequiredArgsConstructor
public class Exam02Controller {

    private final Exam02Service exam02Service;

    @GetMapping("/makeTemplate/{templateId}/{businessId}")
    public String makeTemplate(@PathVariable("templateId") String templateId, @PathVariable("businessId") String businessId) {

        log.info("templateId = {}, businessId = {}", templateId, businessId);
        exam02Service.makeTemplate(templateId, businessId);

        return templateId;
    }
}
