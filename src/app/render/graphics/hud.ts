import { AdvancedDynamicTexture, Line, TextBlock } from '@babylonjs/gui';
import { Scene } from '@babylonjs/core/scene';
import { Vector2 } from '@babylonjs/core';

export class HUD {

  private advancedDynamicTexture: AdvancedDynamicTexture;
  private textBlock: TextBlock;

  constructor(private scene: Scene) {
    this.advancedDynamicTexture = AdvancedDynamicTexture.CreateFullscreenUI('UI', true, scene);
    this.textBlock = new TextBlock();
    this.textBlock.text = '';
    this.textBlock.color = 'white';
    this.textBlock.fontSize = 24;
  }

  drawText(text: string): void {
    this.textBlock.text = text;
    this.advancedDynamicTexture.addControl(this.textBlock);
  }

  drawLine(p1: Vector2, p2: Vector2): void {
    var line = new Line();
    line.x1 = p1.x;
    line.y1 = p1.y;
    line.x2 = p2.x;
    line.y2 = p2.y;
    line.lineWidth = 5;
    line.color = "white";
    this.advancedDynamicTexture.addControl(line);
  }

  clear(): void {
    this.advancedDynamicTexture.clear();
  }
}
