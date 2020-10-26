import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomInputComponent } from './components/custom-input/custom-input.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomButtonComponent } from './components/custom-button/custom-button.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpListenerService } from './services/http-listener.service';
import { environment } from '../../environments/environment';

@NgModule({
  declarations: [CustomInputComponent, CustomButtonComponent],
  exports: [
    CustomInputComponent,
    CustomButtonComponent,
    ReactiveFormsModule
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpListenerService, multi: true},
    {provide: 'STARTPOINT_API_URL', useValue: environment.startPointApiUrl},
  ]
})
export class SharedModule {
}
