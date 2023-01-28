package dev.boiarshinov.testing.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class AllureAttachmentTest {

    @Test
    void addTextAttachmentByAnnotation() {
        attachToReport("attachment content");
    }

    @Attachment
    private String attachToReport(String content) {
        return content;
    }

    @Test
    void addBlobAttachmentByAnnotation() throws IOException {
        try(InputStream inputStream = AllureAttachmentTest.class.getResourceAsStream("/allure/attachment.png")) {
            byte[] bytes = inputStream.readAllBytes();
            attachToReport(bytes);
        }
    }

    @Attachment(type = "image/png")
    private byte[] attachToReport(byte[] content) {
        return content;
    }

    @Test
    void addTextAttachmentByUtils() {
        Allure.addAttachment("attachment", "attachment content");
    }

    @Test
    void addBlobAttachmentByUtils() throws IOException {
        try (InputStream inputStream = AllureAttachmentTest.class.getResourceAsStream("/allure/attachment.png")) {
            Allure.addAttachment("attachment", inputStream);
        }
    }
}
