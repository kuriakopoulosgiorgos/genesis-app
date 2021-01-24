import { Renderable } from './renderable';
import { GraphicsEngine } from './graphics/graphics-engine';
import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-render',
  templateUrl: './render.component.html',
  styleUrls: ['./render.component.css']
})
export class RenderComponent implements OnInit, OnDestroy, Renderable {
  @ViewChild('canvas', { static: true }) canvas: ElementRef<HTMLCanvasElement>;

  private graphicsEngine: GraphicsEngine;
  private destroy = new Subject();

  fps: string = '';
  isFullScreen: boolean;

  constructor() { }

  ngOnInit(): void {
    this.graphicsEngine = new GraphicsEngine(this);
    this.graphicsEngine.start();

    this.graphicsEngine.fpsCount$.pipe(
      takeUntil(this.destroy)
    )
      .subscribe(fps => this.fps = fps.toFixed(3));

    this.graphicsEngine.isFullScreen$.pipe(
      takeUntil(this.destroy)
    )
      .subscribe(isFullScreen => this.isFullScreen = isFullScreen);
  }

  ngAfterViewInit(): void {


  }

  getRenderCanvas(): ElementRef<HTMLCanvasElement> {
    return this.canvas;
  }

  ngOnDestroy(): void {
    this.destroy.next();
    this.destroy.complete();
  }
}
