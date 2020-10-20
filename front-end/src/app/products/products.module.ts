import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductsComponent } from './products.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';

export const routes: Routes = [
  {
    path: '',
    component: ProductsComponent,
    data: {
      title: 'Products'
    }
  }
];

@NgModule({
  declarations: [ProductsComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class ProductsModule { }
