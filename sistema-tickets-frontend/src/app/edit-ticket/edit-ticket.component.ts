import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Form, FormBuilder, FormGroup } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';
import { Cliente } from '../models/cliente.model';
import { ClientesService } from '../services/clientes.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-ticket',
  templateUrl: './edit-ticket.component.html',
  styleUrls: ['./edit-ticket.component.css']
})
export class EditTicketComponent implements OnInit{
  ticketFormGroup!: FormGroup;
  servicios: Servicio[] = [];
  clientes: Cliente[] = [];
  pdfFileUrl!: string;
  tecnicoCodigo!: string | null;
  ticketId!: number;
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


}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ticketId = +params['id'];
    });
    this.tecnicoService.getTicket(this.ticketId).subscribe(ticket => {
      this.tecnicoCodigo = ticket.tecnico?.codigo || null;
      this.ticketFormGroup.patchValue({
        date: ticket.fecha,
        cantidad: ticket.cantidad,
        servicio: ticket.servicio?.nombre,
        priority: ticket.priority,
        instalacionEquipo: ticket.instalacionEquipo,
        instalacionModelo: ticket.instalacionModelo,
        instalacionDireccion: ticket.instalacionDireccion,
        mantenimientoEquipo: ticket.mantenimientoEquipo,
        mantenimientoDescripcion: ticket.mantenimientoDescripcion,
        mantenimientoProxima: ticket.mantenimientoProxima,
        cotizacionCliente: ticket.cotizacionCliente,
        cotizacionMonto: ticket.cotizacionMonto,
        cotizacionDescripcion: ticket.cotizacionDescripcion,
        diagnosticoEquipo: ticket.diagnosticoEquipo,
        diagnosticoProblema: ticket.diagnosticoProblema,
        diagnosticoObservaciones: ticket.diagnosticoObservaciones,
        pagado: ticket.pagado
      });
      this.onServicioChange(ticket.servicio?.nombre || '');
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
    cantidad: this.fb.control(''),
    servicio: this.fb.control(''),
    priority: this.fb.control(''),
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
  } else {
    this.selectedServiceType = '';
  }
  const serv = this.servicios.find(s => s.nombre === servicioNombre);
  if(serv && serv.liderCodigo){
    this.tecnicoCodigo = serv.liderCodigo;
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
  if(this.tecnicoCodigo){
    formData.set('tecnicoCodigo', this.tecnicoCodigo);
  }
  formData.set('date', formattedDate);
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
  formData.set('pagado', this.ticketFormGroup.value.pagado);

  
    console.log(formData);

    this.tecnicoService.editarTicket(this.ticketId, formData).subscribe({
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
