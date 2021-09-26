import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ModelRoutingModule } from './model-routing.module';
import { UploadModelComponent } from './upload-model/upload-model.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [UploadModelComponent],
  imports: [
    CommonModule,
    ModelRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ModelModule { }
