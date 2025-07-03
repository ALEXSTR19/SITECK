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
import { AuthorizationGuard } from './guards/authorization.guard';
import { TecnicoDetallesComponent } from './tecnico-detalles/tecnico-detalles.component';
import { NewTicketComponent } from './new-ticket/new-ticket.component';
import { EditTicketComponent } from './edit-ticket/edit-ticket.component';


import { TecnicoDashboardComponent } from './tecnico-dashboard/tecnico-dashboard.component';
import { LoadServiciosComponent } from './load-servicios/load-servicios.component';
import { LoadClientesComponent } from './load-clientes/load-clientes.component';
import { ClientesComponent } from './clientes/clientes.component';
import { QuotesComponent } from './quotes/quotes.component';

const routes: Routes = [
  { path: "", component: LoginComponent },
  { path: "login", component: LoginComponent },
  {
    path: "admin",
    component: AdminTemplateComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: "home",
        component: HomeComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "profile",
        component: ProfileComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "loadTecnicos",
        component: LoadTecnicosComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "loadServicios",
        component: LoadServiciosComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "loadClientes",
        component: LoadClientesComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "loadTickets",
        component: LoadTicketsComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "dashboard",
        component: DashboardComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "dashboard-tecnico",
        component: TecnicoDashboardComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN", "TECNICO"] }
      },
      {
        path: "tecnicos",
        component: TecnicosComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "clientes",
        component: ClientesComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "tickets",
        component: TicketsComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "tecnico-detalles/:codigo",
        component: TecnicoDetallesComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "new-ticket",
        component: NewTicketComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "edit-ticket/:id",
        component: EditTicketComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      },
      {
        path: "cotizaciones",
        component: QuotesComponent,
        canActivate: [AuthorizationGuard],
        data: { roles: ["ADMIN"] }
      }
    ]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
