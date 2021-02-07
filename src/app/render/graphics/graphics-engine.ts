import { PointerInteractor } from './../controls/pointer-interactor';
import { HUD } from './hud';
import { AugmentedReality } from './augmented-reality';
import { Renderable } from './../renderable';
import {
  Engine,
  Scene,
  WebXRCamera,
} from '@babylonjs/core';
import { ElementRef } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import * as earcut from 'earcut';
(window as any).earcut = earcut;

export class GraphicsEngine {
  private engine: Engine;
  private canvas: ElementRef<HTMLCanvasElement>;

  private initialWidth: number;
  private initialHeight: number;

  private scene: Scene;
  private camera: WebXRCamera;

  private fpsCount = new BehaviorSubject(0);
  fpsCount$ = this.fpsCount.asObservable();

  private isFullScreen = new BehaviorSubject(false);
  isFullScreen$ = this.isFullScreen.asObservable();

  private pointerInteractor: PointerInteractor;
  private HUD: HUD;
  private augmentedReality: AugmentedReality;

  constructor(private renderable: Renderable) { }

  start(): void {
    this.canvas = this.renderable.getRenderCanvas();
    this.engine = new Engine(this.canvas.nativeElement, true);
    this.initialWidth = this.canvas.nativeElement.width;
    this.initialHeight = this.canvas.nativeElement.height;
    window.addEventListener('resize', () => {
      this.engine.resize();
    });

    window.onresize = (_e: any) => {
      this.isFullScreen.next(this.engine.isFullscreen);
      if (!this.engine.isFullscreen) {
        this.canvas.nativeElement.width = this.initialWidth;
        this.canvas.nativeElement.height = this.initialHeight;
        this.engine.resize();
      }
    };

    // creating minimal scene
    this.createScene(this.canvas.nativeElement);
    // running babylonJS
    this.engine.runRenderLoop(() => this.renderLoop());
  }

  private async createScene(canvas: HTMLCanvasElement): Promise<void> {
    this.scene = new Scene(this.engine);
    this.HUD = new HUD(this.scene);
    this.pointerInteractor = new PointerInteractor(this.scene);
    this.augmentedReality = new AugmentedReality(this.scene);
    const xr = await this.augmentedReality.createXRExprerienceAsync();

    this.camera = new WebXRCamera('camera1', this.scene, xr.baseExperience.sessionManager);
    this.camera.attachControl(canvas, true);
  }

  private renderLoop(): void {
    this.scene.render();

    this.fpsCount.next(this.engine.getFps());
  }
}
