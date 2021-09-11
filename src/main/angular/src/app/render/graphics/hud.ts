import { Product } from './../../api/models/product';
import { ProductService } from './../../api/services/product.service';
import { PointerInteractor } from './../controls/pointer-interactor';
import { Axis, Scene, WebXRCamera, TransformNode, Observer, StandardMaterial, Texture, MeshBuilder, Mesh } from '@babylonjs/core';
import { GUI3DManager, MeshButton3D, SpherePanel } from '@babylonjs/gui';
import { Injectable } from '@angular/core';

@Injectable()
export class HUD {

  private panel: SpherePanel | undefined;
  private panelSpawned = false;
  private products: Product[] = [];
  private sceneObserver: Observer<Scene>;

  constructor(private pointerInteractor: PointerInteractor, private productService: ProductService) {
    this.productService.findAll().subscribe(products => {
      this.products = products;
    })
  }


  /**
   * Displays the HUD
   */
  display(scene: Scene, camera: WebXRCamera): void {

    this.pointerInteractor.interactWithScene(scene);
    // Create the 3D UI manager
    let manager = new GUI3DManager(scene);

    this.pointerInteractor.onDoubleTap$.subscribe(() => {
      if (this.panelSpawned) {
        manager.removeControl(this.panel);
        this.panel.dispose();
        scene.onBeforeRenderObservable.remove(this.sceneObserver);
        this.panelSpawned = false;
      } else {
        this.createItemsPanel(manager, camera, scene);
        this.panelSpawned = true;
        console.log('Panel Spawned!');
      }
    });
  }

  createItemsPanel(manager: GUI3DManager, camera: WebXRCamera, scene: Scene): void {

    this.panel = new SpherePanel();
    manager.addControl(this.panel);

    this.panel.margin = 0.1;
    this.panel.columns = 4;
    this.panel.radius

    this.panel.blockLayout = true;

    for (let product of this.products) {
      this.createButton(product, scene);
    }

    this.panel.blockLayout = false;
    let anchor = new TransformNode('anchor', scene);
    anchor.rotation = camera.rotation;
    anchor.setDirection(camera.getDirection(Axis.Z));
    anchor.position = camera.position.add(camera.getDirection(Axis.Z).scale(5));

    this.panel.linkToTransformNode(anchor);
  }


  /**
   * Creates a 3D Button from a product
   * and adds it to the panel
   */
  private createButton(product: Product, scene: Scene): void {

    const mat = new StandardMaterial(`${product.name}-mat`, scene);
    mat.diffuseTexture = new Texture(null, scene);

    const plane = MeshBuilder.CreatePlane(`${product.name}-btn`, { sideOrientation: Mesh.DOUBLESIDE });
    plane.material = mat;
    let button = new MeshButton3D(plane, `${product.name}-btn`);

    this.panel.addControl(button);
  }
}
