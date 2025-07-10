import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TecnicosService } from '../services/tecnicos.service';
import Swal from 'sweetalert2';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-ticket-report',
  templateUrl: './ticket-report.component.html',
  styleUrls: ['./ticket-report.component.css']
})
export class TicketReportComponent {
  reporte = '';
  ticketId!: number;
  fotos: File[] = [];

  constructor(private route: ActivatedRoute, private service: TecnicosService, private router: Router) {}

  ngOnInit(): void {
    this.ticketId = +this.route.snapshot.paramMap.get('id')!;
  }

  agregarFotos(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.fotos.push(...Array.from(input.files));
      input.value = '';
    }
  }

  guardar() {
    this.service.finalizarTicket(this.ticketId, this.reporte, this.fotos).subscribe({
      next: () => {
        Swal.fire('Reporte guardado', 'El ticket fue finalizado', 'success').then(() => {
          this.router.navigate(['/admin/dashboard-tecnico']);
        });
      },
      error: () => {
        Swal.fire('Error', 'No se pudo guardar el reporte', 'error');
      }
    });
  }
}
