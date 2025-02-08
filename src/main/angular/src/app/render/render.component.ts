import { environment } from 'src/environments/environment';
import { Utils } from '../shared/utils';
import { Renderable } from './renderable';
import { GraphicsEngine } from './graphics/graphics-engine';
import {
  Component,
  ElementRef,
  Input,
  OnInit,
  ViewChild
} from '@angular/core';
import { Model } from '../api/models/model';
import { ApiConfiguration } from '../api/api-configuration';

@Component({
  selector: 'app-render-ui',
  templateUrl: './render.component.html',
  styleUrls: ['./render.component.css']
})
export class RenderComponent implements OnInit, Renderable {

  @ViewChild('canvas', { static: true }) canvas: ElementRef<HTMLCanvasElement>;
  @Input()
  model: Model;

  constructor(private apiConfiguration: ApiConfiguration, private graphicsEngine: GraphicsEngine) { }

  ngOnInit(): void {
      let modelRoute = this.apiConfiguration.rootUrl + '/' + environment.api.attachments + '/' + Utils.omitFileName(this.model.attachment.reference) + '/';
      let modelAttachment = this.model.attachment.name;
      this.graphicsEngine.start(this, modelRoute, modelAttachment);
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
}
