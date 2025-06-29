import { Component, Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../services/auth-service.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private auth:AuthService){}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const t = this.auth.getToken();
    const req2 = t ? req.clone({ setHeaders:{Authorization:`Bearer ${t}`}}):req;
    return next.handle(req2);
  }
}

