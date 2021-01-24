import { HUD } from './hud';
import { Renderable } from './../renderable';
import {
  Engine,
  LinesMesh,
  Mesh,
  MeshBuilder,
  Scene,
  SphereBuilder,
  Vector3,
  WebXRAnchorSystem,
  WebXRCamera,
  WebXRFeaturesManager,
  WebXRHitTest
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

  private createLine = false;
  private HUD: HUD;

  private initialP1: Vector3;
  private p1: Vector3;
  private linesMesh: LinesMesh;
  private tempSphere: Mesh;

  constructor(private renderable: Renderable) {}

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

    const xr = await this.scene.createDefaultXRExperienceAsync({
      uiOptions: {
        sessionMode: 'immersive-ar',
        referenceSpaceType: 'unbounded'
      },
      optionalFeatures: true
    });

    this.camera = new WebXRCamera(
      'camera1',
      this.scene,
      xr.baseExperience.sessionManager
    );
    this.camera.attachControl(canvas, true);

    const featureManager = xr.baseExperience.featuresManager;

    this.enableHitTest(featureManager);
  }

  private renderLoop(): void {
    this.HUD.clear();
    this.scene.render();

    this.fpsCount.next(this.engine.getFps());
  }

  private enableHitTest(featureManager: WebXRFeaturesManager): void {
    // featuresManager from the base webxr experience helper
    const hitTest = featureManager.enableFeature(
      WebXRHitTest,
      'latest'
    ) as WebXRHitTest;

    // featuresManager from the base webxr experience helper
    const anchorSystem = featureManager.enableFeature(WebXRAnchorSystem, 'latest') as WebXRAnchorSystem;

    anchorSystem.onAnchorAddedObservable.add(anchor => {
      this.tempSphere = SphereBuilder.CreateSphere('sphere', {diameter: 0.5, updatable: true}, this.scene);
      this.tempSphere.position = this.p1.clone();
      this.tempSphere.isVisible = false;
      anchor.attachedNode = this.tempSphere;
    });

    anchorSystem.onAnchorUpdatedObservable.add(anchor => {
      this.p1 = this.tempSphere.position;
    });

    hitTest.onHitTestResultObservable.add(results => {
      let text = '';
      if (results.length) {
        results.forEach(res => {
          //text += `Pos {x : ${res.position.x} y : ${res.position.y} z : ${res.position.z}}\n`;
        });

        if (this.scene.isPointerCaptured && !this.createLine) {

          this.createLine = true;
          this.p1 = results[0].position.clone();
          this.initialP1 = results[0].position.clone();
          let p2 = this.p1.clone();
          p2.x += 1;
          const points = [this.p1, p2];
          this.linesMesh = MeshBuilder.CreateLines('lines', {points: points, updatable: true}, this.scene);
          console.log("Should create Line!");
          anchorSystem.addAnchorPointUsingHitTestResultAsync(results[0]);
        }

        if (this.createLine){
          const points = [this.p1, results[0].position.clone()];
          this.linesMesh = MeshBuilder.CreateLines('lines', {points: points, updatable:true, instance: this.linesMesh}, this.scene);
          let distance = Vector3.Distance(points[0], points[1]);
          text += `Distance: ${distance.toFixed(3)}m\n`;
        }

      } else {
      }

      this.HUD.drawText(text);
    });
  }
}
