import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TecnicosService } from '../services/tecnicos.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-ticket-report',
  templateUrl: './ticket-report.component.html',
  styleUrls: ['./ticket-report.component.css']
})
export class TicketReportComponent {
  reporte = '';
  ticketId!: number;

  constructor(private route: ActivatedRoute, private service: TecnicosService, private router: Router) {}

  ngOnInit(): void {
    this.ticketId = +this.route.snapshot.paramMap.get('id')!;
  }

  guardar() {
    this.service.finalizarTicket(this.ticketId, this.reporte).subscribe({
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
