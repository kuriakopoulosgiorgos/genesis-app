package test.utils;

import gr.uth.models.Attachment;

public class AttachmentTestUtil {

    private AttachmentTestUtil() {}

    public static Attachment createAttachment(String reference) {
        return createAttachment(reference, "test name", "test description",
                100L, "Text/plain");
    }

    public static Attachment createAttachment(String reference, String name, String description,
                                              Long size, String contentType) {
        var attachment = new Attachment();
        attachment.reference = reference;
        attachment.name = name;
        attachment.description = description;
        attachment.size = size;
        attachment.contentType = contentType;
        return attachment;
    }
}
