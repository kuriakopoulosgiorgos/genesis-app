import { environment } from 'src/environments/environment';
import { ProductService } from './../api/services/product.service';
import { Utils } from '../shared/utils';
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
import { Product } from '../api/models/product';

@Component({
  selector: 'app-render',
  templateUrl: './render.component.html',
  styleUrls: ['./render.component.css']
})
export class RenderComponent implements OnInit, OnDestroy, Renderable {
  @ViewChild('canvas', { static: true }) canvas: ElementRef<HTMLCanvasElement>;

  private destroy = new Subject();

  fps: string = '';

  products: Product[];

  constructor(private graphicsEngine: GraphicsEngine, private productService: ProductService) { }

  ngOnInit(): void {

    this.productService.findAll().subscribe(products => {
      this.products = products.data;
      let productWithModel = this.products.find(product => product.model?.attachment);
      if(productWithModel) {

        let modelRoute = environment.api.attachments + '/' + Utils.omitFileName(productWithModel.model.attachment.reference) + '/';
        let model = productWithModel.model.attachment.name;
        this.graphicsEngine.start(this, modelRoute, model);

        this.graphicsEngine.fpsCount$.pipe(
          takeUntil(this.destroy)
        )
          .subscribe(fps => this.fps = fps.toFixed(3));
      }
    });

  }

  ngAfterViewInit(): void {
    this.canvas.nativeElement.onwheel = (event) =>  {
      event.preventDefault();
    };
  }

  getRenderCanvas(): ElementRef<HTMLCanvasElement> {
    return this.canvas;
  }

  fullScreen(): void {
    this.graphicsEngine.fullScreen();
  }

  ngOnDestroy(): void {
    this.destroy.next();
    this.destroy.complete();
  }
}
