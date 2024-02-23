package info.rue.examples.service.impl;

import info.rue.examples.service.Exam02Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class Exam02ServiceImpl implements Exam02Service {


    @Override
    public void makeTemplate(String templateId, String businessId) {

        switch (templateId) {
            case "dgConv":
                makeTemplateDgConv(businessId);
                break;
            case "java":
                makeTemplateJava();
                break;
            default:
                log.info("templateId {} 존재하지 않음. 확인 필요", templateId);
                break;
        }

    }

    private void makeTemplateDgConv(String businessId) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        String dirPath = "D:\\project\\템플릿\\";
        String targetDirPath = dirPath + businessId + "\\" + sdf.format(cal.getTime()) + "\\";
        String templateFilename = dirPath + "동국대 CONV 샘플.SQL";
        String businessFilename = dirPath + businessId + "_FILE_LIST.txt";
        List<String> businessTableList = readTemplateFile(businessFilename);
        List<String> templateList = readTemplateFile(templateFilename);
        makeTemplateFile(targetDirPath, businessTableList, templateList);
    }

    private void makeTemplateJava() {

    }

    private List<String> readTemplateFile(String filePath) {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String str;
            while ((str = reader.readLine()) != null) {
                log.info(str);
                list.add(str);
            }
            reader.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private void makeTemplateFile(String targetDirPath, List<String> businessTableList, List<String> templateList) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat todaySdf10 = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> replaceParamMap = new HashMap<>();

        Pattern pattern = Pattern.compile("#\\{(.+)}");
        Matcher matcher;

        try {
            Files.createDirectories(Paths.get(targetDirPath));

            for(String businessTable : businessTableList) {
                String[] arrBusinessTableInfo = businessTable.split(",");
                String targetFilePath = targetDirPath + arrBusinessTableInfo[3] + "_" + sdf.format(cal.getTime()) + ".sql";

                replaceParamMap.clear();
                replaceParamMap.put("TOBE_TABLE", arrBusinessTableInfo[1]);
                replaceParamMap.put("PROCEDURE_NAME", arrBusinessTableInfo[3]);
                replaceParamMap.put("TODAY", todaySdf10.format(cal.getTime()));
                replaceParamMap.put("FILENAME", arrBusinessTableInfo[3] + "_" + sdf.format(cal.getTime()) + ".sql");

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilePath), "MS949"));

                for (String s : templateList) {
                    matcher = pattern.matcher(s);
                    while (matcher.find()) {
                        String token = matcher.group();
                        String tokenKey = matcher.group(1);
                        log.info("token, tokenKey =========>>>> {}, {}", token, replaceParamMap.get(tokenKey));
                        s = s.replaceAll(Pattern.quote(token), replaceParamMap.get(tokenKey));
                        log.info(s);
                    }

                    bw.write(s);
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void makeTemplateFile(String targetFilePath, List<String> list) {
        try {
            File file = new File(targetFilePath);
            if(file.exists()) {
                throw new RuntimeException("파일이 존재합니다. [" + targetFilePath +"]");
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(String s: list) {
                writer.write(s);
                writer.newLine();
            }
            writer.flush(); // 버퍼의 남은 데이터를 모두 쓰기
            writer.close(); // 스트림 종료
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
