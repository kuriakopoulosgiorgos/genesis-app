import { ElementRef } from '@angular/core'

export interface Renderable {

  getRenderCanvas(): ElementRef<HTMLCanvasElement>;
}
