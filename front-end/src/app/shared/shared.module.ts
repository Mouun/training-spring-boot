import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomInputComponent } from './components/custom-input/custom-input.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomButtonComponent } from './components/custom-button/custom-button.component';

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
  ]
})
export class SharedModule {
}
