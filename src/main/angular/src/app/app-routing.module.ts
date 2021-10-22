import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: 'products', loadChildren: () => import('./products/products.module').then(m => m.ProductsModule) },
  { path: 'render', loadChildren: () => import('./render/render.module').then(m => m.RenderModule) },
  { path: 'model', loadChildren: () => import('./model/model.module').then(m => m.ModelModule) },
  { path: '',   redirectTo: 'products', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
