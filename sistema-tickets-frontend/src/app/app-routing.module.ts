import { NgModule } from '@angular/core';
import { RouterModule, Routes, UrlTree } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { LoginComponent } from './login/login.component';
import { LoadTecnicosComponent } from './load-tecnicos/load-tecnicos.component';
import { LoadTicketsComponent } from './load-tickets/load-tickets.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TicketsComponent } from './tickets/tickets.component';
import { TecnicosComponent } from './tecnicos/tecnicos.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { AuthGuard } from './guards/auth.guard';
import { AuthorizationGuard } from './guards/authorization.guard.ts';
import { TecnicoDetallesComponent } from './tecnico-detalles/tecnico-detalles.component';
import { NewTicketComponent } from './new-ticket/new-ticket.component';
import { RegisterComponent } from './register/register.component';


import { TecnicoDashboardComponent } from './tecnico-dashboard/tecnico-dashboard.component';
import { LoadServiciosComponent } from './load-servicios/load-servicios.component';
const routes: Routes = [
  { path: "", component: LoginComponent },
  { path: "login", component: LoginComponent },
  { path: "register", component: RegisterComponent },
  {
    path: "admin", component: AdminTemplateComponent,
    canActivate: [AuthGuard], // Assuming AuthGuard is imported and provided in the module
    children: [
      { path: "home", component: HomeComponent },
      { path: "profile", component: ProfileComponent },
      { path: "loadTecnicos", component: LoadTecnicosComponent,
       },
      { path: "loadServicios", component: LoadServiciosComponent,
        canActivate: [AuthorizationGuard], data: { roles: ['ADMIN']},
       },

      { path: "loadTickets", component: LoadTicketsComponent,
        canActivate: [AuthorizationGuard], data: { roles: ['ADMIN'] }
       },
      { path: "dashboard", component: DashboardComponent },
      { path: "dashboard-tecnico", component: TecnicoDashboardComponent },
      { path: "tecnicos", component: TecnicosComponent },
      { path: "tickets", component: TicketsComponent },
      { path: "tecnico-detalles/:codigo",  component: TecnicoDetallesComponent },
      { path: "new-ticket/:codigoTecnico", component: NewTicketComponent }
    ]
  }, // Assuming admin redirects to home

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
