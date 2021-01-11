import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RenderRoutingModule } from './render-routing.module';
import { RenderComponent } from './render.component';


@NgModule({
  declarations: [RenderComponent],
  imports: [
    CommonModule,
    RenderRoutingModule
  ]
})
export class RenderModule { }
