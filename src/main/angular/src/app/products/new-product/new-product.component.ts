import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UploadAttachmentsEvent } from '../../events';
import { Model } from 'src/app/api/models/model';
import { Product } from 'src/app/api/models/product';
import { Attachment } from 'src/app/api/models/attachment';
import { NewProductPresenter } from './new-product-presenter';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { SUPPORTED_MODEL_EXTENSIONS } from 'src/app/constants';

@Component({
  selector: 'app-new-product-ui',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css'],
  providers: [ NewProductPresenter ]
})
export class NewProductComponent implements OnInit, OnDestroy {

  @Input()
  model: Observable<Model>;

  @Input()
  photos: Observable<Attachment[]>;

  @Input()
  loading = false;

  @Output()
  onModelUpload: EventEmitter<UploadAttachmentsEvent> = new EventEmitter();

  @Output()
  onModelDelete: EventEmitter<void> = new EventEmitter();

  @Output()
  onPhotoUpload: EventEmitter<UploadAttachmentsEvent> = new EventEmitter();

  @Output()
  onPhotoDelete: EventEmitter<string> = new EventEmitter();

  @Output()
  onSubmitProduct: EventEmitter<Product> = new EventEmitter();

  private destroy: Subject<void>  = new Subject();

  constructor(private newProductPresenter: NewProductPresenter) { }

  get productForm(): FormGroup {
    return this.newProductPresenter.productForm;
  }

  get uploadedPhotos(): Attachment[] {
    return this.newProductPresenter.uploadedPhotos;
  }

  get photosForm(): FormGroup {
    return this.newProductPresenter.photosForm;
  }

  get product(): Product {
    return this.newProductPresenter.product;
  }

  get modelInvalid(): boolean {
    return this.newProductPresenter.modelInvalid;
  }

  get supportedFormats(): string[] {
    return SUPPORTED_MODEL_EXTENSIONS;
  }

  get rootUrl(): string {
    return this.newProductPresenter.rootUrl ;
  }

  ngOnInit(): void {
    this.newProductPresenter.init(this.model, this.photos);
    this.newProductPresenter.onModelUpload$.pipe(
      takeUntil(this.destroy)
    ).subscribe(uploadAttachmentEvent => this.onModelUpload.emit(uploadAttachmentEvent));

    this.newProductPresenter.onModelDelete$.pipe(
      takeUntil(this.destroy)
    ).subscribe(_v => this.onModelDelete.emit());

    this.newProductPresenter.onPhotoUpload$.pipe(
      takeUntil(this.destroy)
    ).subscribe(uploadAttachmentEvent => this.onPhotoUpload.emit(uploadAttachmentEvent));

    this.newProductPresenter.onPhotoDelete$.pipe(
      takeUntil(this.destroy)
    ).subscribe(photoReference => this.onPhotoDelete.emit(photoReference));

    this.newProductPresenter.onSubmitProduct$.pipe(
      takeUntil(this.destroy)
    ).subscribe(product => this.onSubmitProduct.emit(product));
  }

  modelUpload(event: Event): void {
    this.newProductPresenter.modelUpload(event);
  }

  deleteModel(): void {
    this.newProductPresenter.deleteModel();
  }

  photoUpload(event: Event): void {
    this.newProductPresenter.photoUpload(event)
  }

  deletePhoto(i: number): void {
    this.newProductPresenter.deletePhoto(i);
  }

  submitProduct(): void {
    this.newProductPresenter.submitProduct();
  }

  ngOnDestroy(): void {
    this.destroy.next();
    this.destroy.complete();
  }
}
