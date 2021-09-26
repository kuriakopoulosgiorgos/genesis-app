import { PointerInteractor } from './controls/pointer-interactor';
import { GraphicsEngine } from './graphics/graphics-engine';
import { NgModule } from '@angular/core';

import { RenderRoutingModule } from './render-routing.module';
import { RenderComponent } from './render.component';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [RenderComponent],
  imports: [
    CommonModule,
    NgbModule,
    RenderRoutingModule
  ],
  providers: [GraphicsEngine, PointerInteractor]
})
export class RenderModule { }
