import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductsComponent } from './products.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { ProductRowActionsComponent } from './components/product-row-actions/product-row-actions.component';
import { ClickOutsideModule } from 'ng-click-outside';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { ProductResolver, ProductService } from '../shared/services/product.service';

export const routes: Routes = [
  {
    path: '',
    component: ProductsComponent,
    data: {
      title: 'Products'
    },
    resolve: {
      products: ProductResolver
    }
  }
];

@NgModule({
  declarations: [ProductsComponent, ProductRowActionsComponent],
  imports: [
    CommonModule,
    SharedModule,
    ClickOutsideModule,
    RouterModule.forChild(routes),
    SweetAlert2Module.forRoot()
  ],
  exports: [RouterModule],
  providers: [ProductService, ProductResolver]
})
export class ProductsModule {
}
