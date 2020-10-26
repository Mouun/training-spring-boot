import { Inject, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { catchError, finalize, map } from 'rxjs/operators';
import { TokenService } from './token.service';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class HttpListenerService implements HttpInterceptor {

  private excludedRoutesForAuthHeader = [
    'login'
  ];

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private authService: AuthService,
    @Inject('STARTPOINT_API_URL') private startPointApiUrl: string
  ) {
  }

  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.startPointApiUrl) {
      request = request.clone({ url: this.startPointApiUrl + request.url });
    }

    if (!this.excludedRoutesForAuthHeader.includes(request.url.split('/').pop())) {
      request = this.addAuthHeader(request);
    }

    return this.handleRequest(request, next);
  }

  private handleRequest(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      map((event: HttpEvent<any>) => this.extractData(event)),
      catchError((errorResponse: HttpErrorResponse) => {
        return throwError(errorResponse);
      }),
      finalize(() => {
      }));
  }

  private extractData(event: HttpEvent<any>): HttpEvent<any> {
    if (event instanceof HttpResponse) {
      const jsonResponse = this.getResponseAsJson(event.body);
      let data: string = null;
      if (!!jsonResponse) {
        data = jsonResponse.data;
      }
      return event.clone({ body: data });
    }
    return event;
  }

  private addAuthHeader = (request: HttpRequest<any>): HttpRequest<any> => {
    console.log('ok');
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${this.tokenService.getToken().token}`
      }
    });
    return request;
  };

  public getResponseAsJson(responseBody: any): any {
    let jsonResponse = responseBody;
    if (!responseBody) {
      return null;
    }
    if (typeof responseBody === 'string') {
      jsonResponse = JSON.parse(responseBody);
    }
    return jsonResponse;
  }
}
