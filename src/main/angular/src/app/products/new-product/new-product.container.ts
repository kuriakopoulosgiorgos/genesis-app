import { Component, OnInit } from '@angular/core';
import { Attachment } from 'src/app/api/models/attachment';
import { Model } from 'src/app/api/models/model';
import { AttachmentService } from 'src/app/api/services/attachment.service';
import { UploadAttachmentsEvent } from 'src/app/events';
import { Subject, BehaviorSubject, Observable, merge } from 'rxjs';
import { Product } from 'src/app/api/models/product';
import { ProductService } from 'src/app/api/services/product.service';
import { Router } from '@angular/router';
import { SUPPORTED_MODEL_EXTENSIONS } from 'src/app/constants';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.container.html'
})
export class NewProductContainerComponent implements OnInit {

  private model: Subject<Model> = new BehaviorSubject(null);
  model$: Observable<Model> = this.model.asObservable();

  private uploadedModel: Attachment[] = []
  private uploadedPhotos: Attachment[] = []
  private photos: Subject<Attachment[]> = new BehaviorSubject(this.uploadedPhotos);
  photos$: Observable<Attachment[]> = this.photos.asObservable();

  loading = false;

  constructor(private attachmentService: AttachmentService, private productService: ProductService, private router: Router) { }

  ngOnInit(): void {
  }

  uploadModel(uploadAttachmentsEvent: UploadAttachmentsEvent): void {
    this.loading = true;
    this.attachmentService.uploadAttachments({body: {attachmentMetaData: uploadAttachmentsEvent.attachmentMetaData, file: uploadAttachmentsEvent.files}})
    .subscribe(res => {
      this.loading = false;
      this.uploadedModel = [...res];
      let modelAttachment = res.find(attachment => SUPPORTED_MODEL_EXTENSIONS.find(supportedExtension => attachment.reference.includes(supportedExtension)));
      let model = {
        attachment: modelAttachment
      };
      this.model.next(model);
    })
  }

  deleteModel(): void {
    this.loading = true;
    let deletions = [];
    this.uploadedModel.forEach(attachment => {
      deletions.push(this.attachmentService.deleteByReference({ body: attachment.reference }));
    });

    merge(...deletions)
    .subscribe(_res => {
      this.loading = false;
      this.model.next(null);
    });
  }

  uploadPhoto(uploadAttachmentsEvent: UploadAttachmentsEvent): void {
    this.loading = true;
    this.attachmentService.uploadAttachments({body: {attachmentMetaData: uploadAttachmentsEvent.attachmentMetaData, file: uploadAttachmentsEvent.files}})
    .subscribe(res => {
      this.loading = false;
      this.uploadedPhotos = [...this.uploadedPhotos, ...res];
      this.photos.next(this.uploadedPhotos);
    });
  }

  deletePhoto(reference: string) {
    this.loading = true;
    this.attachmentService.deleteByReference({ body: reference})
    .subscribe(_v => {
      this.loading = false;
      this.uploadedPhotos = this.uploadedPhotos.filter(photo => photo.reference !== reference);
      this.photos.next(this.uploadedPhotos);
    });
  }

  submitProduct(product: Product): void {
    this.loading = true;
    this.productService.create({body: product})
    .subscribe(_product => {
      this.loading = false;
      this.router.navigate(['/']);
    });
  }
}
