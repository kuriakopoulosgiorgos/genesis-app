import { PointerInteractor } from './../controls/pointer-interactor';
import { Scene, WebXRCamera, TransformNode } from '@babylonjs/core';
import { CylinderPanel, GUI3DManager, HolographicButton } from '@babylonjs/gui';

export class HUD {

  private panel: CylinderPanel | undefined;
  private panelSpawned = false;
  constructor(private scene: Scene, private pointerInteractor: PointerInteractor, private camera: WebXRCamera) { }


  /**
   * Displays the HUD
   */
  display(): void {

    // Create the 3D UI manager
    let manager = new GUI3DManager(this.scene);

    this.pointerInteractor.onDoubleTap$.subscribe(() => {
      if (this.panelSpawned) {
        manager.removeControl(this.panel);
        this.panelSpawned = false;
      } else {
        this.createItemsPanel(manager);
        this.panelSpawned = true;
      }
    });
  }

  async createItemsPanel(manager: GUI3DManager): Promise<void> {
    let node = new TransformNode('panel');
    node.rotationQuaternion = this.camera.rotationQuaternion.clone();
    node.position = this.camera.getFrontPosition(5);

    this.panel = new CylinderPanel();
    this.panel.margin = 0.2;

    manager.addControl(this.panel);

    this.panel.linkToTransformNode(node);
    this.panel.columns = 5;

    // Let's add some buttons!
    let addButton = (index) => {
      let button = new HolographicButton("orientation");
      this.panel.addControl(button);

      button.text = "Button #" + index;
    }

    this.panel.blockLayout = true;

    for (let index = 0; index < 20; index++) {
      addButton(index);
    }

    this.panel.blockLayout = false;
  }
}
