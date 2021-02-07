import { PointerEventTypes } from '@babylonjs/core/Events/pointerEvents';
import { Scene } from '@babylonjs/core/scene';

export class PointerInteractor {

  private _isPointerDown = false;

  constructor(private scene: Scene) {
    this.scene.onPointerObservable.add(pointerInfo => {
      switch (pointerInfo.type) {
        case PointerEventTypes.POINTERDOWN:
          this._isPointerDown = true;
          break;
        case PointerEventTypes.POINTERUP:
          this._isPointerDown = false;
          break;
      }
    });
  }

  isPointerDown(): boolean {
    return this._isPointerDown;
  }
}
