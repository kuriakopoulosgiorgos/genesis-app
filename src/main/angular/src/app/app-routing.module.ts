import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: 'render', loadChildren: () => import('./render/render.module').then(m => m.RenderModule) },
  { path: 'model', loadChildren: () => import('./model/model.module').then(m => m.ModelModule) },
  { path: '',   redirectTo: 'model', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
