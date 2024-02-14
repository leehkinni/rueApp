package info.rue.examples.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples/exam01")
@Slf4j
public class Exam01Controller {

    @Value("${config.file.loc1}")
    private String configFileLoc1;

    @Value("${config.file.loc2}")
    private String configFileLoc2;

    @Value("${config.file.loc3}")
    private String configFileLoc3;

    @Value("${config.file.loc4}")
    private String configFileLoc4;

    @Value("${config.file.loc5}")
    private String configFileLoc5;

    @Value("${config.file.loc6}")
    private String configFileLoc6;

    @Value("${config.file.loc7}")
    private String configFileLoc7;

    @Value("${config.file.loc8}")
    private String configFileLoc8;


    @GetMapping("/getConfigFileLoc")
    public String getTestKey() {

        log.info("configFileLoc1 =======>>> {}", configFileLoc1);
        log.info("configFileLoc2 =======>>> {}", configFileLoc2);
        log.info("configFileLoc3 =======>>> {}", configFileLoc3);
        log.info("configFileLoc4 =======>>> {}", configFileLoc4);
        log.info("configFileLoc5 =======>>> {}", configFileLoc5);
        log.info("configFileLoc6 =======>>> {}", configFileLoc6);
        log.info("configFileLoc7 =======>>> {}", configFileLoc7);
        log.info("configFileLoc8 =======>>> {}", configFileLoc8);

        return configFileLoc1;
    }
}
