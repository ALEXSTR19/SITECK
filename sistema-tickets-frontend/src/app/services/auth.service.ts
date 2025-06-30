import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

public username: any;
public codigoTecnico: string | undefined;
public isAuthenticated: boolean = false;
public roles: string[] = [];

  constructor(private router: Router, private http: HttpClient) { }

  public login(username: string, password: string): Observable<boolean> {
    return this.http
      .post<Usuario>(`${environment.backendHost}/api/auth/login`, { username, password })
      .pipe(
        tap((user: Usuario) => {
          this.username = user.username;
          this.roles = [user.role];
          this.codigoTecnico = user.codigoTecnico;
          this.isAuthenticated = true;
        }),
        map(() => true)
      );
  }

  public register(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${environment.backendHost}/api/auth/register`, usuario);
  }

  logout(){
    this.isAuthenticated = false;
    this.roles = [];
    this.username = undefined;
    this.codigoTecnico = undefined;
    this.router.navigateByUrl("/login");
  }
}
