import {
  Component,
  ElementRef,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {
  FlyCamera,
  HemisphericLight,
  PointLight,
  Mesh,
  SceneLoader,
  StandardMaterial,
  MeshBuilder,
  Tools,
  MultiPointerScaleBehavior,
} from '@babylonjs/core';
import { Engine } from '@babylonjs/core/Engines/engine';
import { Color3, Vector3 } from '@babylonjs/core/Maths/math';
import { Scene } from '@babylonjs/core/scene';
import * as earcut from "earcut";
(window as any).earcut = earcut;

@Component({
  selector: 'app-render',
  templateUrl: './render.component.html',
  styleUrls: ['./render.component.css']
})
export class RenderComponent implements OnInit {
  @ViewChild('canvas', { static: true }) canvas: ElementRef<HTMLCanvasElement>;

  @Output() engine: Engine;
  @Output() scene: Scene;
  @Output() camera: FlyCamera;

  fps: string;

  private initialWidth: number;
  private initialHeight: number;

  private ground: Mesh;
  private sphere: Mesh;
  private hemisphericLight: HemisphericLight;
  private pointLight: PointLight;

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.engine = new Engine(this.canvas.nativeElement, true);
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

    this.scene = new Scene(this.engine);

    // creating camera
    this.camera = this.createCamera(this.scene);

    // allow mouse deplacement
    this.camera.attachControl(true);

    // creating minimal scene
    this.createScene(this.scene, this.canvas.nativeElement);

    // running babylonJS
    this.engine.runRenderLoop(() => this.renderLoop());
  }

  private createCamera(scene: Scene): FlyCamera {
    const camera = new FlyCamera('camera1', new Vector3(0, 100, 160), scene);

    camera.setTarget(Vector3.Zero());

    return camera;
  }

  private createScene(scene: Scene, canvas: HTMLCanvasElement): void {
    this.createLights();
    this.createGround();
    this.createSphere();
    this.createCar();

    SceneLoader.ImportMesh('', 'assets/Dude/', 'dude.babylon', this.scene);
  }

  toggleFullscreen(): void {
    this.engine.enterFullscreen(false);
  }

  private renderLoop(): void {
    this.scene.render();

    this.fps = this.engine.getFps().toFixed(3);
    this.sphere.rotateAround(
      this.ground.position,
      new Vector3(0, 1, 0),
      this.engine.getDeltaTime() / 1000
    );

    this.pointLight.position = this.sphere.position.add(
      this.ground.position.subtract(this.sphere.position).scale(0.4)
    );
  }

  private createLights(): void {
    this.hemisphericLight = new HemisphericLight(
      'light',
      new Vector3(0, 1, 0),
      this.scene
    );
    this.pointLight = new PointLight('pointLight', Vector3.Zero(), this.scene);
    this.hemisphericLight.intensity = 0.1;
    this.pointLight.intensity = 0.7;
  }

  private createGround(): void {
    this.ground = Mesh.CreateGround('ground', 400, 400, 2, this.scene);

    let groundMaterial = new StandardMaterial('spereTexture', this.scene);
    groundMaterial.ambientColor = new Color3(0.2, 0.2, 0.2);
    groundMaterial.diffuseColor = new Color3(0, 1, 0);
    groundMaterial.specularColor = Color3.Black();

    this.ground.material = groundMaterial;
  }

  private createSphere(): void {
    const diameter = 30;
    this.sphere = Mesh.CreateSphere('sphere', 16, diameter, this.scene, true);

    this.sphere.position.y = this.ground.position.y + diameter / 2;
    this.sphere.position.x = 5 * diameter;
    this.sphere.position.z = 25;
  }

  private createCar(): void {
    //base
    const outline = [
      new Vector3(-0.3, 0, -0.1),
      new Vector3(0.2, 0, -0.1),
  ]

    //curved front
    for (let i = 0; i < 20; i++) {
        outline.push(new Vector3(0.2 * Math.cos(i * Math.PI / 40), 0, 0.2 * Math.sin(i * Math.PI / 40) - 0.1));
    }



    //top
    outline.push(new Vector3(0, 0, 0.1));
    outline.push(new Vector3(-0.3, 0, 0.1));

  //back formed automatically

    const car = MeshBuilder.ExtrudePolygon("car", {shape: outline, depth: 0.2});


    const scale = 10;
    const scaleVector = new Vector3(scale, scale, scale);
    car.scaling = scaleVector;

    const moveDir = this.camera.target.subtract(this.camera.position).normalize();

    const distance = 10;
    car.position = this.camera.position.add(moveDir.scale(distance));
    car.rotation.subtractInPlace(this.camera.rotation);

    const rightBackWheel = MeshBuilder.CreateCylinder("rightBackWheel", {diameter: 0.125, height: 0.05});

    rightBackWheel.parent = car;
    rightBackWheel.position.z = -0.1;
    rightBackWheel.position.x = -0.2;
    rightBackWheel.position.y = 0.035;

    const rightFrontWheel = rightBackWheel.clone("rightFrontWheel");
    rightFrontWheel.parent = car;
    rightFrontWheel.position.x = 0.1;

    const leftBackWheel = rightBackWheel.clone("leftFronleftBackWheeltWheel");
    leftBackWheel.parent = car;
    leftBackWheel.position.x = -0.2;
    leftBackWheel.position.y = -0.2 - 0.035;

    const leftFrontWheel = rightBackWheel.clone("leftFrontWheel");
    leftFrontWheel.parent = car;
    leftFrontWheel.position.x = 0.1;
    leftFrontWheel.position.y = -0.2 - 0.035;

    car.addBehavior(new MultiPointerScaleBehavior());

  }
}
