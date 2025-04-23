package io.Yoo_SH.backend_weather_system.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 애플리케이션 시작 시 로그 디렉토리를 생성하는 클래스
 */
@Component
public class LogDirectoryInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(LogDirectoryInitializer.class);

    @Value("${logging.file.path:logs}")
    private String logPath;

    /**
     * 애플리케이션 시작 시 실행됩니다.
     * 로그 디렉토리가 없으면 생성합니다.
     */
    @Override
    public void run(String... args) throws Exception {
        createLogDirectories();
    }

    /**
     * 로그 디렉토리를 생성합니다.
     */
    private void createLogDirectories() {
        try {
            // 기본 로그 디렉토리 생성
            Path logDir = Paths.get(logPath);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
                logger.info("로그 디렉토리 생성: {}", logDir.toAbsolutePath());
            }

            // 아카이브 로그 디렉토리 생성
            Path archiveDir = Paths.get(logPath, "archived");
            if (!Files.exists(archiveDir)) {
                Files.createDirectories(archiveDir);
                logger.info("아카이브 로그 디렉토리 생성: {}", archiveDir.toAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("로그 디렉토리 생성 중 오류 발생: {}", e.getMessage(), e);
        }
    }
} 