import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../shared/services/auth.service';
import { TokenService } from '../shared/services/token.service';
import { LogInCredentials } from '../shared/models/LogInCredentials';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private tokenService: TokenService
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  public logIn(): void {
    this.authService.logIn(this.loginForm.getRawValue() as LogInCredentials).subscribe(token => {
      this.tokenService.setToken(token);
      this.router.navigateByUrl('/');
    });
  }
}
