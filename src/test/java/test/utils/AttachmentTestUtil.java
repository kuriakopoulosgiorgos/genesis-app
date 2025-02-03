package test.utils;

import gr.uth.models.Attachment;

public class AttachmentTestUtil {

    private AttachmentTestUtil() {}

    public static Attachment createAttachment(String reference) {
        return createAttachment(reference, "test name", "test description",
                100L, "Text/plain");
    }

    public static Attachment createAttachment(String reference, String contentType) {
        return createAttachment(reference, "test name", "test description",
                100L, contentType);
    }

    public static Attachment createAttachment(String reference, String name, String description,
                                              Long size, String contentType) {
        var attachment = new Attachment();
        attachment.setReference(reference);
        attachment.setName(name);
        attachment.setDescription(description);
        attachment.setSize(size);
        attachment.setContentType(contentType);
        return attachment;
    }
}
