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

public username: string | undefined;
public codigoTecnico: string | undefined;
public isAuthenticated = false;
public roles: string[] = [];

  constructor(private router: Router, private http: HttpClient) {
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    const data = localStorage.getItem('auth_user');
    if (data) {
      const user = JSON.parse(data) as {
        username: string;
        roles: string[];
        codigoTecnico?: string;
      };
      this.username = user.username;
      this.roles = user.roles;
      this.codigoTecnico = user.codigoTecnico;
      this.isAuthenticated = true;
    }
  }

  public login(username: string, password: string): Observable<boolean> {
    return this.http
      .post<Usuario>(`${environment.backendHost}/api/auth/login`, { username, password })
      .pipe(
        tap((user: Usuario) => {
          this.username = user.username;
          this.roles = [user.role];
          this.codigoTecnico = user.codigoTecnico;
          this.isAuthenticated = true;
          localStorage.setItem(
            'auth_user',
            JSON.stringify({ username: this.username, roles: this.roles, codigoTecnico: this.codigoTecnico })
          );
        }),
        map(() => true)
      );
  }

  public getProfile(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${environment.backendHost}/api/auth/profile/${username}`);
  }

  public register(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${environment.backendHost}/api/auth/register`, usuario);
  }

  logout(){
    this.isAuthenticated = false;
    this.roles = [];
    this.username = undefined;
    this.codigoTecnico = undefined;
    localStorage.removeItem('auth_user');
    this.router.navigateByUrl("/login");
  }
}
