import { AugmentedReality } from './graphics/augmented-reality';
import { HUD } from './graphics/hud';
import { PointerInteractor } from './controls/pointer-interactor';
import { GraphicsEngine } from './graphics/graphics-engine';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RenderRoutingModule } from './render-routing.module';
import { RenderComponent } from './render.component';


@NgModule({
  declarations: [RenderComponent],
  imports: [
    CommonModule,
    RenderRoutingModule
  ],
  providers: [GraphicsEngine, PointerInteractor, HUD, AugmentedReality]
})
export class RenderModule { }