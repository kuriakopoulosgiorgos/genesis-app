import { Injectable } from '@angular/core';
import { Scene } from '@babylonjs/core'
import { WebXRDefaultExperience, WebXRAnchorSystem } from "@babylonjs/core";

@Injectable()
export class AugmentedReality {

  constructor() { }


  /**
   * creates the augmented reality experience
   */
  async createXRExprerienceAsync(scene: Scene): Promise<WebXRDefaultExperience> {

    const xr = scene.createDefaultXRExperienceAsync({
      uiOptions: {
        sessionMode: 'immersive-ar',
        referenceSpaceType: 'unbounded'
      },
      optionalFeatures: true
    });

    xr.then(xr => { this.manageAnchors(xr) });

    return xr;
  }


  /**
   * Manages anchors
   * @param xr the default web xr expirence
   */
  private manageAnchors(xr: WebXRDefaultExperience): void {

    // featuresManager from the base webxr experience helper
    const anchorSystem = xr.baseExperience.featuresManager.enableFeature(WebXRAnchorSystem, 'latest') as WebXRAnchorSystem;

  }
}
