import { Renderable } from './../renderable';
import 'babylonjs';
import 'babylonjs-gui';
import 'babylonjs-loaders';
import { ElementRef, Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import * as earcut from 'earcut';
(window as any).earcut = earcut;

@Injectable()
export class GraphicsEngine {

  private engine: BABYLON.Engine;
  private canvas: ElementRef<HTMLCanvasElement>;

  private initialWidth: number;
  private initialHeight: number;
  private scene: BABYLON.Scene;

  private fpsCount = new BehaviorSubject(0);
  fpsCount$ = this.fpsCount.asObservable();

  constructor() { }

  start(renderable: Renderable, modelRootURL: string, model: string): void {
    this.canvas = renderable.getRenderCanvas();
    this.engine = new BABYLON.Engine(this.canvas.nativeElement, true);
    this.initialWidth = this.canvas.nativeElement.width;
    this.initialHeight = this.canvas.nativeElement.height;
    window.addEventListener('resize', () => {
      this.engine.resize();
    });

    window.onresize = (_e: any) => {
      if (!this.engine.isFullscreen) {
        this.canvas.nativeElement.width = this.initialWidth;
        this.canvas.nativeElement.height = this.initialHeight;
        this.engine.resize();
      }
    };

    // creating minimal scene
    this.scene = this.createScene(this.canvas.nativeElement, modelRootURL, model);
    // running babylonJS
    this.engine.runRenderLoop(() => this.renderLoop());
  }

  fullScreen(): void {
    this.engine.enterFullscreen(false);
  }

  private createScene(canvas: HTMLCanvasElement, modelRootURL: string, model: string): BABYLON.Scene {

    var scene = new BABYLON.Scene(this.engine);
    scene.clearColor = new BABYLON.Color4(1, 1, 1);
    var light = new BABYLON.HemisphericLight("HemiLight", new BABYLON.Vector3(0, 1, 0), scene);
    var camera = new BABYLON.ArcRotateCamera("cam", Math.PI / 2, Math.PI / 2, 10, BABYLON.Vector3.Zero(), scene);

    camera.wheelDeltaPercentage = 0.01;
    camera.attachControl(canvas, true);

    BABYLON.SceneLoader.ImportMesh("", modelRootURL, model, scene, (meshes) => {
      camera.zoomOnFactor = Math.PI / 2;
      camera.zoomOn(meshes);
    });

    return scene;
  }

  private renderLoop(): void {
    this.scene.render();
    this.fpsCount.next(this.engine.getFps());
  }
}
