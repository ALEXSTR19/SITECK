import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { LoadTecnicosComponent } from './load-tecnicos/load-tecnicos.component';
import { LoadTicketsComponent } from './load-tickets/load-tickets.component';
import { LoginComponent } from './login/login.component';
import { TicketsComponent } from './tickets/tickets.component';
import { TecnicosComponent } from './tecnicos/tecnicos.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthGuard } from './guards/auth.guard';
import { AuthorizationGuard } from './guards/authorization.guard.ts';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TecnicoDetallesComponent } from './tecnico-detalles/tecnico-detalles.component';
import { NewTicketComponent } from './new-ticket/new-ticket.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { RegisterComponent } from './register/register.component';
import { TecnicoDashboardComponent } from './tecnico-dashboard/tecnico-dashboard.component';
import { LoadServiciosComponent } from './load-servicios/load-servicios.component';
@NgModule({
  declarations: [
    AppComponent,
    AdminTemplateComponent,
    HomeComponent,
    ProfileComponent,
    LoadTecnicosComponent,
    LoadTicketsComponent,
    LoginComponent,
    TicketsComponent,
    TecnicosComponent,
    DashboardComponent,
    TecnicoDetallesComponent,
    NewTicketComponent,
    RegisterComponent,
    TecnicoDashboardComponent,
    LoadServiciosComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule, MatSidenavModule, MatListModule,
    MatCardModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatTableModule,
    HttpClientModule, MatPaginatorModule, MatSortModule, MatProgressSpinnerModule,
    MatDatepickerModule, MatNativeDateModule, MatSelectModule, BrowserModule
  ],
  providers: [
    provideClientHydration(),
    provideAnimationsAsync(), AuthGuard, AuthorizationGuard, provideHttpClient(withFetch())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
