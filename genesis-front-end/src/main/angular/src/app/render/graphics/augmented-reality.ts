import { Injectable } from '@angular/core';
import { Scene, SceneLoader, AbstractMesh, WebXRHitTest } from '@babylonjs/core'
import { WebXRDefaultExperience, WebXRAnchorSystem } from "@babylonjs/core";
import "@babylonjs/loaders";

@Injectable()
export class AugmentedReality {

  private reticle: AbstractMesh;

  constructor() { }


  /**
   * creates the augmented reality experience
   */
  async createXRExprerienceAsync(scene: Scene): Promise<WebXRDefaultExperience> {

    const xr = await scene.createDefaultXRExperienceAsync({
      uiOptions: {
        sessionMode: 'immersive-ar',
        referenceSpaceType: 'unbounded',
        requiredFeatures: ['local', 'hit-test']
      }
    });

    xr.baseExperience.sessionManager.isSessionSupportedAsync('immersive-ar').then(isSupported => {
      if (!isSupported) {
        xr.baseExperience.exitXRAsync();
        xr.dispose();
        alert('immersive-ar is not Supported!');
      }
    });

    this.initAR(scene);
    this.manageAnchors(xr);
    this.manageHitTest(xr);

    return xr;
  }


  private initAR(scene: Scene): void {

    SceneLoader.Append("./assets/Reticle/", "reticle.gltf", scene, (scene) => {
      this.reticle = scene.getMeshByName('Torus');
      this.reticle.isVisible = false;
    });
  }


  /**
   * Manages anchors
   * @param xr the default web xr expirence
   */
  private manageAnchors(xr: WebXRDefaultExperience): void {

    // featuresManager from the base webxr experience helper
    const anchorSystem = xr.baseExperience.featuresManager.enableFeature(WebXRAnchorSystem, 'latest') as WebXRAnchorSystem;

  }


  /**
 * Manages hit test
 * @param xr the default web xr expirence
 */
  private manageHitTest(xr: WebXRDefaultExperience): void {

    // featuresManager from the base webxr experience helper
    const hitTest = xr.baseExperience.featuresManager.enableFeature(WebXRHitTest, 'latest') as WebXRHitTest;
    hitTest.onHitTestResultObservable.add((results) => {
      if(results.length > 0) {
        results[0].transformationMatrix.decompose(undefined, this.reticle.rotationQuaternion, this.reticle.position);
        this.reticle.position.x *= -1;
        this.reticle.isVisible = true;
      } else {
        this.reticle.isVisible = false;
      }
    });

  }
}
