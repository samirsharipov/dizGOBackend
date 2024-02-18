package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.entity.Lesson;
import uz.pdp.springsecurity.entity.LessonUser;
import uz.pdp.springsecurity.entity.Test;
import uz.pdp.springsecurity.mapper.TestMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.LessonRepository;
import uz.pdp.springsecurity.repository.LessonUserRepository;
import uz.pdp.springsecurity.repository.TestRepository;
import uz.pdp.springsecurity.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestService {
    private final LessonRepository lessonRepository;
    private final TestRepository testRepository;
    private final TestMapper testMapper;
    private final LessonUserRepository lessonUserRepository;
    private final UserRepository userRepository;

    public ApiResponse add(MultipartFile file, UUID lessonId) {

        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        if (file.isEmpty() || file.getContentType() == null) return new ApiResponse("FILE NOT FOUND", false);
        if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
            return new ApiResponse("FILE TYPE MUST BE .DOCX", false);
        Lesson lesson = optionalLesson.get();
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())){
            List<Test> testList = new ArrayList<>();
            for (IBodyElement bodyElement : document.getBodyElements()) {
                if (bodyElement instanceof XWPFTable table) {
                    if (table.getRows().size() < 2)return new ApiResponse( "0 TEST UPLOADED", false);
                    List<XWPFTableRow> rowList = table.getRows().subList(1, table.getRows().size());
                    for (XWPFTableRow row : rowList) {
                        if (row.getCell(1).getText().trim().equals(""))continue;
                        if (row.getCell(2).getText().trim().equals(""))continue;
                        if (row.getCell(3).getText().trim().equals(""))continue;
                        if (row.getCell(4).getText().trim().equals(""))continue;
                        if (row.getCell(5).getText().trim().equals(""))continue;
                        Test test = new Test(
                                lesson,
                                row.getCell(1).getText().trim(),
                                row.getCell(2).getText().trim(),
                                row.getCell(3).getText().trim(),
                                row.getCell(4).getText().trim(),
                                row.getCell(5).getText().trim()
                        );
                        testList.add(test);
                    }
                }
            }
            lesson.setHasTest(true);
            lessonRepository.save(lesson);
            testRepository.saveAll(testList);
            return new ApiResponse("success", true, testList.size());
        } catch (IOException e) {
            return new ApiResponse("ERROR", false);
        }
    }

    public ApiResponse getAll(UUID lessonId) {
        if (!lessonRepository.existsById(lessonId)) return new ApiResponse("LESSON NOT FOUND", false);
        List<Test> testList = testRepository.findAllByLessonId(lessonId);
        if (testList.isEmpty()) return new ApiResponse("TEST NOT FOUND", false);
        return new ApiResponse( true, testMapper.toDtoList(testList));
    }

    public ApiResponse generate(UUID lessonId, UUID userId) {
        if (!lessonRepository.existsById(lessonId)) return new ApiResponse("LESSON NOT FOUND", false);
        if (!userRepository.existsById(userId)) return new ApiResponse("USER NOT FOUND", false);
        Optional<LessonUser> optionalLessonUser = lessonUserRepository.findByUserIdAndLessonId(userId, lessonId);
        if (optionalLessonUser.isEmpty())
            return new ApiResponse("LESSON_USER NOT FOUND", false);
        LessonUser lessonUser = optionalLessonUser.get();
        if (lessonUser.isSolveTest())
            new ApiResponse("YOU SOLVED THE TEST", false);
        List<Test> testList = testRepository.findAllByLessonId(lessonId);
        if (testList.isEmpty()) return new ApiResponse("TEST NOT FOUND", false);
        if (testList.size() < 10) return new ApiResponse("TESTS NOT ENOUGH", false);
        Collections.shuffle(testList);
        return new ApiResponse( true, testMapper.toDtoList(testList.subList(0, 10)));
    }

    public ApiResponse getSample(HttpServletResponse response) {
        File file = new File("upload/sample.docx");
        if (file.exists()) {
            try (InputStream inputStream = new BufferedInputStream( new FileInputStream(file))){
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", String.format("attachment; filename=" + file.getName()));
                response.setContentLength((int) file.length());
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                return new ApiResponse("SUCCESS", true);
            } catch (Exception e) {
                return new ApiResponse("ERROR", false);
            }
        }
        return new ApiResponse("ERROR", false);
    }

    @Transactional
    public ApiResponse deleteAll(UUID lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        if (!testRepository.existsAllByLessonId(lessonId)) return new ApiResponse("TEST NOT FOUND", false);
        try {
            testRepository.deleteAllByLessonId(lessonId);
            Lesson lesson = optionalLesson.get();
            lesson.setHasTest(false);
            lessonRepository.save(lesson);
            return new ApiResponse("SUCCESS", true);
        } catch (Exception e) {
            return new ApiResponse("ERROR", false);
        }
    }

    public ApiResponse deleteById(List<UUID> idList) {
        if (idList.isEmpty()) return new ApiResponse("ID LIST EMPTY", false);
        try {
            testRepository.deleteAllById(idList);
            return new ApiResponse("SUCCESS", true);
        } catch (Exception e) {
            return new ApiResponse("ERROR", false);
        }
    }
}
