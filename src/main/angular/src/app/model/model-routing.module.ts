import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UploadModelComponent } from './upload-model/upload-model.component';

const routes: Routes = [
  { path: '', component: UploadModelComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ModelRoutingModule { }
