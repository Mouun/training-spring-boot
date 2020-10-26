import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';
import { Token } from '../models/Token';

export const TOKEN_KEY = 'jwt_token';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }

  public getToken(): Token {
    return {
      token: localStorage.getItem(TOKEN_KEY)
    };
  }

  public setToken(token: Token): void {
    localStorage.setItem(TOKEN_KEY, token.token);
  }

  public removeToken(): void {
    localStorage.removeItem(TOKEN_KEY);
  }

  public isAuthenticated(): boolean {
    return !(this.getToken().token === null);
  }

  public isAdmin(): boolean {
    return this.getRoles().some(r => r === 'ADMIN');
  }

  public hasOneOfRoles(expectedRoles: string[]): boolean {
    return this.isAuthenticated() && this.getRoles().some(r => expectedRoles.indexOf(r) >= 0 || r === 'ADMIN');
  }

  private getRoles(): string[] {
    const token = this.getToken().token;

    if (token === undefined) {
      return [];
    }

    const decoded = jwt_decode(token);

    console.log(decoded);

    if (decoded.Role !== undefined) {
      if (decoded.Role instanceof Array) {
        return decoded.Role;
      } else {
        return [decoded.Role];
      }
    } else {
      return [];
    }
  }
}
