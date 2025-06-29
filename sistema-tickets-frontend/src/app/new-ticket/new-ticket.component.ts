import { Component, OnInit } from '@angular/core';
import { Form, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TicketType } from '../models/tecnicos.model';
import { TecnicosService } from '../services/tecnicos.service';
import { title } from 'process';
import { text } from 'stream/consumers';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-new-ticket',
  templateUrl: './new-ticket.component.html',
  styleUrls: ['./new-ticket.component.css']
})
export class NewTicketComponent implements OnInit{
  ticketFormGroup!: FormGroup;
  codigoTecnico!: string;
  tiposTickets: string[] = [];
  pdfFileUrl!: string;
  constructor(private fb:FormBuilder, private activatedRoute:ActivatedRoute, private tecnicoService: TecnicosService) { 
    // Initialize the form group and other properties here if needed


}  ngOnInit(): void {
  for(let elt in TicketType){
    let value = TicketType[elt];
    if (typeof value === 'string') {
      this.tiposTickets.push(value);
    }
  }
  this.codigoTecnico = this.activatedRoute.snapshot.params['codigoTecnico'];
  this.ticketFormGroup = this.fb.group({
    date: this.fb.control(''),
    cantidad: this.fb.control(''),
    type: this.fb.control(''),
    codigoTecnico: this.fb.control(this.codigoTecnico),
    fileSource: this.fb.control(''),
    fileName: this.fb.control(''),
  });
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
  formData.set('type', this.ticketFormGroup.value.type);
  formData.set('codigoTecnico', this.ticketFormGroup.value.codigoTecnico);
  formData.append('file', this.ticketFormGroup.value.fileSource);

  
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
