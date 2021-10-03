import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiModule } from '../api/api.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ApiModule,
    TranslateModule
  ],
  exports: [
    TranslateModule,
  ],
  providers: [
  ]
})
export class SharedModule { }
