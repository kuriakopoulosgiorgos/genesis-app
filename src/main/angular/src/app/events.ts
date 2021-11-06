import { AttachmentMetaData } from './api/models/attachment-meta-data';

export interface UploadAttachmentsEvent {
  files: File [];
  attachmentMetaData: { [key: string]: AttachmentMetaData; };
}
