import { Subject } from 'rxjs';
import { PointerEventTypes } from '@babylonjs/core/Events/pointerEvents';
import { Scene } from '@babylonjs/core/scene';

export class PointerInteractor {

  private _isPointerDown = false;

  // work-around for not working double tap
  private pointerTap = false;
  // The duration between each tap to be considered a double tap
  private static readonly doubleTapInterval = 600;
  private _onDoubleTap = new Subject();
  onDoubleTap$ = this._onDoubleTap.asObservable();

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

    this.scene.onPointerObservable.add(eventData => {
      // double tap
      if (this.pointerTap && eventData.type === PointerEventTypes.POINTERTAP) {
        this._onDoubleTap.next();
        this.pointerTap = false;
      }

      // first tap
      if (!this.pointerTap && eventData.type === PointerEventTypes.POINTERTAP) {
        this.pointerTap = true;
        setTimeout(() => {
          this.pointerTap = false;
        }, PointerInteractor.doubleTapInterval);
      }
    });
  }


  isPointerDown(): boolean {
    return this._isPointerDown;
  }
}
