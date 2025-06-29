import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { MatToolbar } from '@angular/material/toolbar';

@Component({
  selector: 'app-admin-template',
  templateUrl: './admin-template.component.html',
  styleUrls: ['./admin-template.component.css']
})
export class AdminTemplateComponent {

constructor(public authService: AuthService, private router: Router) { }

logout() {
  this.authService.isAuthenticated = false;
  this.authService.roles = [];
  this.authService.username = undefined;
  this.router.navigateByUrl("/login");
}
}
