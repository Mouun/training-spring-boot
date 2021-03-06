import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreComponent } from './core.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from '../home/home.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoginComponent } from '../login/login.component';
import { AuthGuardService } from '../shared/services/auth-guard.service';
import { AuthService } from '../shared/services/auth.service';
import { ProductResolver, ProductService } from '../shared/services/product.service';

export const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuardService],
    component: HomeComponent,
    data: {
      title: 'Home'
    },
    resolve: {
      products: ProductResolver
    }
  },
  {
    path: 'login',
    component: LoginComponent,
    loadChildren: () => import('../login/login.module').then(m => m.LoginModule)
  },
  {
    path: 'products',
    canActivate: [AuthGuardService],
    loadChildren: () => import('../products/products.module').then(m => m.ProductsModule)
  },
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '/404' }
];

@NgModule({
  declarations: [CoreComponent, NotFoundComponent],
  exports: [
    RouterModule,
    CoreComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes),
    SharedModule,
    LoadingBarModule,
    LoadingBarRouterModule,
    LoadingBarHttpClientModule
  ],
  bootstrap: [CoreComponent],
  providers: [ProductService, AuthService, ProductResolver]
})
export class CoreModule {
}
