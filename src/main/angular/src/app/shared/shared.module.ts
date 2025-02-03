import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TruncatePipe } from './truncate.pipe';
import { NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [
    TruncatePipe
  ],
  imports: [
    CommonModule,
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
