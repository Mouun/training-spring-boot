import { Component, Input, OnInit } from '@angular/core';
import { ControlContainer, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'app-custom-input',
  templateUrl: './custom-input.component.html',
  styleUrls: ['./custom-input.component.scss'],
  viewProviders: [{ provide: ControlContainer, useExisting: FormGroupDirective }]
})
export class CustomInputComponent implements OnInit {

  @Input() public type = 'text';
  @Input() public inputLabel: string;
  @Input() public inputPlaceholder: string;
  @Input() public controlName: string;
  @Input() public optional = false;
  @Input() public showForgotPassword = false;

  public passwordVisible = false;

  constructor() { }

  ngOnInit(): void {
  }

  public get computedType(): string {
    if (this.type === 'password' && this.passwordVisible) { return 'text'; }
    if (this.type === 'password' && !this.passwordVisible) { return 'password'; }
    if (this.type === 'number') { return 'number'; }
  }
}
