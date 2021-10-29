import { Validators, FormBuilder } from '@angular/forms';
import { Product } from 'src/app/api/models/product';
import { Observable, Subject } from 'rxjs';
import { Model } from 'src/app/api/models/model';
import { Attachment } from 'src/app/api/models/attachment';
import { UploadAttachmentsEvent } from 'src/app/events';
import { Injectable } from '@angular/core';
import { SUPPORTED_MODEL_EXTENSIONS } from 'src/app/constants';

@Injectable()
export class NewProductPresenter {
  private onModelUpload: Subject<UploadAttachmentsEvent> = new Subject();
  onModelUpload$ = this.onModelUpload.asObservable();

  private onModelDelete: Subject<void> = new Subject();
  onModelDelete$ = this.onModelDelete.asObservable();

  private onPhotoUpload: Subject<UploadAttachmentsEvent> = new Subject();
  onPhotoUpload$ = this.onPhotoUpload.asObservable();

  private onPhotoDelete: Subject<string> = new Subject();
  onPhotoDelete$ = this.onPhotoDelete.asObservable();

  private onSubmitProduct: Subject<Product> = new Subject();
  onSubmitProduct$ = this.onSubmitProduct.asObservable();

  productForm = this.formBuilder.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
    price: ['', [Validators.required]],
  });

  modelInvalid = false;

  uploadedPhotos: Attachment[] = [];

  photosForm = this.formBuilder.group({
    file: [null],
    description: [null],
  });

  product: Product = {
    name: '',
    description: '',
    photos: [],
    price: 0,
    model: undefined,
  };

  constructor(private formBuilder: FormBuilder) {}

  init(model: Observable<Model>, photos: Observable<Attachment[]>): void {
    this.productForm.valueChanges.subscribe((productForm) => {
      this.product = { ...this.product, ...(productForm as Product) };
    });

    model.subscribe((model) => {
      this.product.model = model;
    });

    photos.subscribe((photos) => {
      this.product.photos = [...photos];
      this.uploadedPhotos = [...photos];
    });
  }

  modelUpload(event: Event): void {
    let files: File[] = [];
    let target = event.target as HTMLInputElement;

    let isSupportedModel = false;
    for (var i = 0; i < target.files.length; i++) {
      let file = target.files[i];
      if(SUPPORTED_MODEL_EXTENSIONS.find(supportedExtension => file.name.includes(supportedExtension))) {
        isSupportedModel = true;
      }
      files.push(file);
    }

    if(!isSupportedModel) {
      this.modelInvalid = true;
      return;
    }

    this.modelInvalid = false;

    let uploadAttachmentsEvent: UploadAttachmentsEvent = {
      files: files,
      attachmentMetaData: {},
    };

    this.onModelUpload.next(uploadAttachmentsEvent);
  }

  deleteModel(): void {
    this.onModelDelete.next();
  }

  photoUpload(event: Event): void {
    let target = event.target as HTMLInputElement;
    let file: File = target.files[0];
    this.uploadedPhotos = [...this.uploadedPhotos, { name: file.name }];

    let descriptionControl = this.photosForm.get('description');
    let photoDescription = descriptionControl.value;
    let attachmentMetaData = photoDescription?.length > 0 ? { '1': { description: photoDescription } } : {};
    this.photosForm.reset();
    let uploadAttachmentsEvent: UploadAttachmentsEvent = {
      files: [file],
      attachmentMetaData: attachmentMetaData,
    };
    this.onPhotoUpload.next(uploadAttachmentsEvent);
  }

  deletePhoto(i: number): void {
    this.onPhotoDelete.next(this.uploadedPhotos[i].reference);
  }

  submitProduct(): void {
    this.onSubmitProduct.next(this.product);
  }
}
