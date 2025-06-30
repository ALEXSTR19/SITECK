import { Component, OnInit } from '@angular/core';
import { Form, FormBuilder, FormGroup } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-new-ticket',
  templateUrl: './new-ticket.component.html',
  styleUrls: ['./new-ticket.component.css']
})
export class NewTicketComponent implements OnInit{
  ticketFormGroup!: FormGroup;
  servicios: Servicio[] = [];
  pdfFileUrl!: string;
  selectedServicio = '';
  // Fields specific to each type of servicio will be handled through the reactive form
  constructor(
    private fb: FormBuilder,
    private tecnicoService: TecnicosService,
    private servicioService: ServiciosService
  ) {
    // Initialize the form group and other properties here if needed


}  ngOnInit(): void {
  this.servicioService.getServicios().subscribe({
    next: servicios => (this.servicios = servicios),
    error: err => console.error('Error al cargar servicios', err)
  });

  this.ticketFormGroup = this.fb.group({
    date: this.fb.control(''),
    cantidad: this.fb.control(''),
    servicio: this.fb.control(''),
    fileSource: this.fb.control(null),
    fileName: this.fb.control(''),
    // Campos para Instalación
    instalacionEquipo: this.fb.control(''),
    instalacionModelo: this.fb.control(''),
    instalacionDireccion: this.fb.control(''),
    // Campos para Mantenimiento
    mantenimientoEquipo: this.fb.control(''),
    mantenimientoDescripcion: this.fb.control(''),
    mantenimientoProxima: this.fb.control(''),
    // Campos para Cotización
    cotizacionCliente: this.fb.control(''),
    cotizacionMonto: this.fb.control(''),
    cotizacionDescripcion: this.fb.control(''),
    // Campos para Diagnóstico
    diagnosticoEquipo: this.fb.control(''),
    diagnosticoProblema: this.fb.control(''),
    diagnosticoObservaciones: this.fb.control(''),
    // Campos MECP
    mecpNombre: this.fb.control(''),
    mecpTelefono: this.fb.control(''),
    mecpDireccion: this.fb.control(''),
    mecpModelo: this.fb.control(''),
    mecpNombreEquipo: this.fb.control(''),
    mecpAccesorios: this.fb.control(''),
    mecpDiagnostico: this.fb.control(''),
    mecpDetalles: this.fb.control(''),
  });

  this.ticketFormGroup.get('servicio')?.valueChanges.subscribe(v => this.selectedServicio = v);
}

 selectFile(event: any){
  if (event.target.files && event.target.files.length > 0) {
    let file = event.target.files[0];
    this.ticketFormGroup.patchValue({
      fileSource: file,
      fileName: file.name
    });
    this.pdfFileUrl = window.URL.createObjectURL(file);
  }
}

guardarTicket() {
  let date: Date = new Date(this.ticketFormGroup.value.date);
  let formattedDate = date.toISOString().slice(0, 10); // "2025-06-24"

  console.log('Archivo a enviar:', this.ticketFormGroup.value.fileSource);

  let formData = new FormData();
  formData.set('date', formattedDate);
  formData.set('cantidad', this.ticketFormGroup.value.cantidad);
  formData.set('servicio', this.ticketFormGroup.value.servicio);
  if (this.ticketFormGroup.value.fileSource) {
    formData.append('file', this.ticketFormGroup.value.fileSource);
  }
  // Adjuntar información adicional según el tipo de servicio
  formData.set('instalacionEquipo', this.ticketFormGroup.value.instalacionEquipo);
  formData.set('instalacionModelo', this.ticketFormGroup.value.instalacionModelo);
  formData.set('instalacionDireccion', this.ticketFormGroup.value.instalacionDireccion);
  formData.set('mantenimientoEquipo', this.ticketFormGroup.value.mantenimientoEquipo);
  formData.set('mantenimientoDescripcion', this.ticketFormGroup.value.mantenimientoDescripcion);
  formData.set('mantenimientoProxima', this.ticketFormGroup.value.mantenimientoProxima);
  formData.set('cotizacionCliente', this.ticketFormGroup.value.cotizacionCliente);
  formData.set('cotizacionMonto', this.ticketFormGroup.value.cotizacionMonto);
  formData.set('cotizacionDescripcion', this.ticketFormGroup.value.cotizacionDescripcion);
  formData.set('diagnosticoEquipo', this.ticketFormGroup.value.diagnosticoEquipo);
  formData.set('diagnosticoProblema', this.ticketFormGroup.value.diagnosticoProblema);
  formData.set('diagnosticoObservaciones', this.ticketFormGroup.value.diagnosticoObservaciones);
  formData.set('mecpNombre', this.ticketFormGroup.value.mecpNombre);
  formData.set('mecpTelefono', this.ticketFormGroup.value.mecpTelefono);
  formData.set('mecpDireccion', this.ticketFormGroup.value.mecpDireccion);
  formData.set('mecpModelo', this.ticketFormGroup.value.mecpModelo);
  formData.set('mecpNombreEquipo', this.ticketFormGroup.value.mecpNombreEquipo);
  formData.set('mecpAccesorios', this.ticketFormGroup.value.mecpAccesorios);
  formData.set('mecpDiagnostico', this.ticketFormGroup.value.mecpDiagnostico);
  formData.set('mecpDetalles', this.ticketFormGroup.value.mecpDetalles);

  
    console.log(formData);

    this.tecnicoService.guardarTicket(formData).subscribe({
      next: value => {
        Swal.fire({
          title: "Ticket Guardado",
          text: "El ticket se ha guardado correctamente.",
          icon: "success",

        })
      },
      error: err => {
        Swal.fire({
          title: "Error al guardar el ticket",
          text: "Ocurrió un error al guardar el ticket.",
          icon: "error",
        })
      }
    })

}
}
