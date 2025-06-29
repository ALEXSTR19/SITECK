import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

@Injectable()
export class AuthService {
  private tokenKey = 'JWT_TOKEN';
  constructor(private http: HttpClient) {}

  login(u: string, p: string){
    return this.http.post('/authenticate',{username:u,password:p})
      .pipe(tap((res:any)=>localStorage.setItem(this.tokenKey,res.token)));
  }



  logout(){ localStorage.removeItem(this.tokenKey); }

  getToken(){ return localStorage.getItem(this.tokenKey); }
isLoggedIn(): boolean {
  return !!localStorage.getItem('token'); // o como guardes el token JWT
}
}

