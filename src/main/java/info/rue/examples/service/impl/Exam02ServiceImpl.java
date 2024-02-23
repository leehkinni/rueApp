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

    private static final String BASE_DIR_PATH = "D:\\project\\템플릿\\";
    private static final String BASE_JAVA_TEMPLATE_PATH = "D:\\project\\템플릿\\java\\src\\";
    private static final String BASE_JAVA_OUTPUT_PATH = "D:\\project\\템플릿\\java\\output\\";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    private final SimpleDateFormat todaySdf10 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void makeTemplate(String templateId, String businessId) {

        switch (templateId) {
            case "dgConv":
                makeTemplateDgConv(businessId);
                break;
            case "java":
                makeTemplateJava(businessId);       // businessId = javaMappingFilename
                break;
            default:
                log.info("templateId {} 존재하지 않음. 확인 필요", templateId);
                break;
        }

    }

    private void makeTemplateDgConv(String businessId) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        String targetDirPath = BASE_DIR_PATH + businessId + "\\" + sdf.format(cal.getTime()) + "\\";
        String templateFilename = BASE_DIR_PATH + "동국대 CONV 샘플.SQL";
        String businessFilename = BASE_DIR_PATH + businessId + "_FILE_LIST.txt";
        List<String> businessTableList = readTemplateFile(businessFilename);
        List<String> templateList = readTemplateFile(templateFilename);
        makeTemplateFile(targetDirPath, businessTableList, templateList);
    }

    private void makeTemplateJava(String businessId) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        String targetDirPath = sdf.format(cal.getTime());
        String javaTemplateFilename = BASE_DIR_PATH + businessId + ".txt";

        File javaTemplatePath = new File(BASE_JAVA_TEMPLATE_PATH);
        makeTemplatePathJava(javaTemplatePath, javaTemplateFilename, targetDirPath);
    }

    private void makeTemplatePathJava(File javaTemplatePath, String javaTemplateFilename, String currFileLoc) {

        log.info("javaTemplatePath.getAbsoluteFile() = {}, javaTemplateFilename = {}", javaTemplatePath.getAbsoluteFile(), javaTemplateFilename);

        if(javaTemplatePath.isFile()) {
            throw new RuntimeException("지정한 정보는 디렉토리가 아닙니다. [" + javaTemplatePath.getAbsoluteFile() + "]");
        }

        File[] files = javaTemplatePath.listFiles();
        log.info("arrFilename = {}", (Object) files);

        if(files != null) {
            for(File file : files) {
                if(file.isFile()) {
                    log.info("file ======>>> {}", file.getName());
                    List<String> paramList = readTemplateFile(javaTemplateFilename);
                    List<String> templateList = readTemplateFile(file.getAbsolutePath());
                    makeTemplateJavaFile(BASE_JAVA_OUTPUT_PATH + "\\" + currFileLoc, file.getName(), paramList, templateList);
                } else {
                    log.info("directory ======>>> {}", file.getName());
                    try {
                        Files.createDirectories(Paths.get(BASE_JAVA_OUTPUT_PATH + currFileLoc + "\\" + file.getName()));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    makeTemplatePathJava(file, javaTemplateFilename, currFileLoc + "\\" + file.getName());
                }
            }
        }

    }

    private void makeTemplateJavaFile(String targetDirPath, String targetFilename, List<String> paramList, List<String> templateList) {

        Calendar cal = Calendar.getInstance();
        Map<String, String> replaceParamMap = new HashMap<>();

        Pattern pattern = Pattern.compile("#\\{(.+)}");
        Matcher matcher;

        try {
            for(String param : paramList) {
                String[] arrBusinessTableInfo = param.split(":");
                String targetFilePath = targetDirPath + "\\" + targetFilename;

                replaceParamMap.clear();
                replaceParamMap.put(arrBusinessTableInfo[0].trim(), arrBusinessTableInfo[1].trim());

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilePath)));

                for (String s : templateList) {
                    bw.write(getReplaceString(s, pattern, replaceParamMap));
                    bw.newLine();
                }

                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    private void makeTemplateFile(String targetDirPath, List<String> businessTableList, List<String> templateList) {

        Calendar cal = Calendar.getInstance();
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
                    bw.write(getReplaceString(s, pattern, replaceParamMap));
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

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for(String s: list) {
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getReplaceString(String src, Pattern pattern, Map<String, String> replaceParamMap) {
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            String token = matcher.group();
            String tokenKey = matcher.group(1);
            log.info("token, tokenKey =========>>>> {}, {}", token, replaceParamMap.get(tokenKey));
            src = src.replaceAll(Pattern.quote(token), replaceParamMap.get(tokenKey));
            log.info(src);
        }

        return src;
    }
}
