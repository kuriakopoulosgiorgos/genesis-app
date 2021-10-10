import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiModule } from '../api/api.module';
import { TruncatePipe } from './truncate.pipe';
import { NgbPopover, NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [
    TruncatePipe
  ],
  imports: [
    CommonModule,
    ApiModule,
    TranslateModule,
    NgbPopoverModule
  ],
  exports: [
    TranslateModule,
    TruncatePipe,
    NgbPopoverModule
  ],
  providers: [
  ]
})
export class SharedModule { }
