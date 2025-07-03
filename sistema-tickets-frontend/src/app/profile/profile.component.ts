import { Component, OnInit } from '@angular/core';
import { Usuario } from '../models/usuario.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  usuario?: Usuario;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const username = this.authService.username;
    if (username) {
      this.authService.getProfile(username).subscribe(u => this.usuario = u);
    }
  }
}
