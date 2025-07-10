import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Form, FormBuilder, FormGroup } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';
import { Cliente } from '../models/cliente.model';
import { ClientesService } from '../services/clientes.service';
import Swal from 'sweetalert2';
import { Tecnico } from '../models/tecnicos.model';
@Component({
  selector: 'app-new-ticket',
  templateUrl: './new-ticket.component.html',
  styleUrls: ['./new-ticket.component.css']
})
export class NewTicketComponent implements OnInit{
  ticketFormGroup!: FormGroup;
  servicios: Servicio[] = [];
  clientes: Cliente[] = [];
  pdfFileUrl!: string;
  tecnicoCodigo!: string | null;
  tecnicos: Tecnico[] = [];
  selectedServiceType: string = '';
  // Fields specific to each type of servicio will be handled through the reactive form
  constructor(
    private fb: FormBuilder,
    private tecnicoService: TecnicosService,
    private servicioService: ServiciosService,
    private clientesService: ClientesService,
    private route: ActivatedRoute
  ) {
    // Initialize the form group and other properties here if needed


}  ngOnInit(): void {
  this.route.queryParams.subscribe(params => {
    this.tecnicoCodigo = params['codigo'];
  });
  this.servicioService.getServicios().subscribe({
    next: servicios => (this.servicios = servicios),
    error: err => console.error('Error al cargar servicios', err)
  });
  this.clientesService.getClientes().subscribe({
    next: clientes => (this.clientes = clientes),
    error: err => console.error('Error al cargar clientes', err)
  });

  this.ticketFormGroup = this.fb.group({
    date: this.fb.control(''),
    horaVisita: this.fb.control(''),
    cantidad: this.fb.control(''),
    servicio: this.fb.control(''),
    priority: this.fb.control(''),
    tecnicoCodigo: this.fb.control(this.tecnicoCodigo || ''),
    clienteId: this.fb.control(''),
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
    // Campos para Levantamiento
    levantamientoCamaras: this.fb.control(''),
    levantamientoVideoportero: this.fb.control(''),
    levantamientoAlarma: this.fb.control(''),
    levantamientoControlAcceso: this.fb.control(''),
    levantamientoControlAsistencia: this.fb.control(''),
    levantamientoRedWifi: this.fb.control(''),
    levantamientoCercoElectrico: this.fb.control(''),
    showLevantamientoCamaras: this.fb.control(false),
    showLevantamientoVideoportero: this.fb.control(false),
    showLevantamientoAlarma: this.fb.control(false),
    showLevantamientoControlAcceso: this.fb.control(false),
    showLevantamientoControlAsistencia: this.fb.control(false),
    showLevantamientoRedWifi: this.fb.control(false),
    showLevantamientoCercoElectrico: this.fb.control(false),
    pagado: this.fb.control(false),
  });

}

onServicioChange(servicioNombre: string){
  const nombre = (servicioNombre || '').toUpperCase();
  if(nombre.includes('INSTAL')){
    this.selectedServiceType = 'INSTALACION';
  } else if(nombre.includes('MANTEN')){
    this.selectedServiceType = 'MANTENIMIENTO';
  } else if(nombre.includes('COTIZ')){
    this.selectedServiceType = 'COTIZACION';
  } else if(nombre.includes('DIAGN')){
    this.selectedServiceType = 'DIAGNOSTICO';
  } else if(nombre.includes('LEVANT')){
    this.selectedServiceType = 'LEVANTAMIENTO';
  } else {
    this.selectedServiceType = '';
  }

  if(servicioNombre){
    this.tecnicoService.getTecnicosPorServicio(servicioNombre).subscribe({
      next: tecs => this.tecnicos = tecs,
      error: err => console.error('Error al cargar tecnicos por servicio', err)
    });
    const serv = this.servicios.find(s => s.nombre === servicioNombre);
    if(serv && serv.liderCodigo){
      this.ticketFormGroup.patchValue({tecnicoCodigo: serv.liderCodigo});
    }
  } else {
    this.tecnicos = [];
  }
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
  const codigoSeleccionado = this.ticketFormGroup.value.tecnicoCodigo || this.tecnicoCodigo;
  if(codigoSeleccionado){
    formData.set('tecnicoCodigo', codigoSeleccionado);
  }
  formData.set('date', formattedDate);
  formData.set('horaVisita', this.ticketFormGroup.value.horaVisita);
  formData.set('cantidad', this.ticketFormGroup.value.cantidad);
  formData.set('servicio', this.ticketFormGroup.value.servicio);
  formData.set('priority', this.ticketFormGroup.value.priority);
  if(this.ticketFormGroup.value.clienteId){
    formData.set('clienteId', this.ticketFormGroup.value.clienteId);
  }
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
  formData.set('levantamientoCamaras', this.ticketFormGroup.value.levantamientoCamaras);
  formData.set('levantamientoVideoportero', this.ticketFormGroup.value.levantamientoVideoportero);
  formData.set('levantamientoAlarma', this.ticketFormGroup.value.levantamientoAlarma);
  formData.set('levantamientoControlAcceso', this.ticketFormGroup.value.levantamientoControlAcceso);
  formData.set('levantamientoControlAsistencia', this.ticketFormGroup.value.levantamientoControlAsistencia);
  formData.set('levantamientoRedWifi', this.ticketFormGroup.value.levantamientoRedWifi);
  formData.set('levantamientoCercoElectrico', this.ticketFormGroup.value.levantamientoCercoElectrico);
  formData.set('pagado', this.ticketFormGroup.value.pagado);

  
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
