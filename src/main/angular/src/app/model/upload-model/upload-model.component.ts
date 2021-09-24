import { AttachmentService } from '../../api/services/attachment.service';
import { AttachmentMetaData } from '../../api/models/attachment-meta-data';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-upload-model',
  templateUrl: './upload-model.component.html',
  styleUrls: ['./upload-model.component.css']
})
export class UploadModelComponent implements OnInit {

  files: File [] = [];
  attachmentMetaData: { [key: string]: AttachmentMetaData; } = {};

   uploadForm = new FormGroup({
    file: new FormControl('', [Validators.required])
  });

  constructor(private attachmentService: AttachmentService) { }

  ngOnInit(): void {}

  get formControls(){
    return this.uploadForm.controls;
  }

  onFileChange(event) {

        for (var i = 0; i < event.target.files.length; i++) {
            let file = event.target.files[i];
            this.files.push(file);
        }
  }

  submit(){
    this.attachmentService.uploadAttachments({body: {attachmentMetaData: this.attachmentMetaData, file: this.files}})
      .subscribe(res => {
        console.log(res);
        alert('Uploaded Successfully.');
      })
  }
}
