import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { TecnicosService } from '../services/tecnicos.service';
import { MatCard } from '@angular/material/card';
import { Cliente } from '../models/cliente.model';
import { ClientesService } from '../services/clientes.service';

declare const jspdf: any;

interface QuoteItem {
  description: string;
  quantity: number;
  price: number;
}

@Component({
  selector: 'app-quotes',
  templateUrl: './quotes.component.html',
  styleUrls: ['./quotes.component.css']
})
export class QuotesComponent implements OnInit {
  selectedClienteId: number | null = null;
  selectedCliente: Cliente | null = null;
  clientes: Cliente[] = [];
  items: QuoteItem[] = [];
  newItem: QuoteItem = { description: '', quantity: 1, price: 0 };

  constructor(private clientesService: ClientesService) {}

  ngOnInit(): void {
    this.clientesService.getClientes().subscribe({
      next: clientes => (this.clientes = clientes),
      error: err => console.error('Error al cargar clientes', err)
    });
  }

  updateSelectedCliente() {
    this.selectedCliente = this.clientes.find(c => c.id === this.selectedClienteId) || null;
  }

  addItem() {
    if (this.newItem.description) {
      this.items.push({ ...this.newItem });
      this.newItem = { description: '', quantity: 1, price: 0 };
    }
  }

  removeItem(index: number) {
    this.items.splice(index, 1);
  }

  total(): number {
    return this.items.reduce((sum, item) => sum + item.quantity * item.price, 0);
  }

  exportPDF() {
    const { jsPDF } = jspdf;
    const doc = new jsPDF();
    let y = 10;
    if (this.selectedCliente) {
      const c = this.selectedCliente;
      doc.text(`Cliente: ${c.nombre} ${c.apellido}`, 10, y); y += 10;
      if (c.email) { doc.text(`Email: ${c.email}`, 10, y); y += 10; }
      if (c.telefono) { doc.text(`Teléfono: ${c.telefono}`, 10, y); y += 10; }
      if (c.direccion) { doc.text(`Dirección: ${c.direccion}`, 10, y); y += 10; }
      if (c.ciudad) { doc.text(`Ciudad: ${c.ciudad}`, 10, y); y += 10; }
      if (c.codigoPostal) { doc.text(`Código Postal: ${c.codigoPostal}`, 10, y); y += 10; }
    }
    this.items.forEach(item => {
      doc.text(`${item.description} - ${item.quantity} x $${item.price}`, 10, y);
      y += 10;
    });
    doc.text(`Total: $${this.total()}`, 10, y + 10);
    doc.save('cotizacion.pdf');
  }
}
