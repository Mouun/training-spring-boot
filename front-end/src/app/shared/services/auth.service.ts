import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Token } from '../models/Token';
import { LogInCredentials } from '../models/LogInCredentials';

@Injectable()
export class AuthService {

  private url = '/';

  constructor(private http: HttpClient) { }

  public logIn = (credentials: LogInCredentials): Observable<Token> => {
    return this.http.post<Token>(`${this.url}/login`, credentials);
  }
}
