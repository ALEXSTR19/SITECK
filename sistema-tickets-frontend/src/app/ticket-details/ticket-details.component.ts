import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { TecnicosService } from '../services/tecnicos.service';
import { TicketHistory } from '../models/ticket-history.model';
import { Ticket } from '../models/tecnicos.model';

@Component({
  selector: 'app-ticket-details',
  templateUrl: './ticket-details.component.html',
  styleUrls: ['./ticket-details.component.css']
})
export class TicketDetailsComponent implements OnInit {
  histories: TicketHistory[] = [];
  dataSource = new MatTableDataSource<TicketHistory>();
  displayedColumns = ['timestamp','action','changes','previousStatus','newStatus'];
  ticketId!: number;
  ticket?: Ticket;
  reporteFotosArray: string[] = [];

  constructor(private route: ActivatedRoute, private service: TecnicosService){}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ticketId = +params['id'];
      this.service.getTicket(this.ticketId).subscribe(t => {
        this.ticket = t;
        if (t.reporteFotos) {
          this.reporteFotosArray = t.reporteFotos.split(',');
        }
      });
      this.service.getTicketHistory(this.ticketId).subscribe((data: TicketHistory[]) => {
        this.histories = data;
        this.dataSource.data = this.histories;
      });
    });
  }

  restore(){
    this.service.restoreTicket(this.ticketId).subscribe(() => {
      this.service.getTicketHistory(this.ticketId).subscribe((d: TicketHistory[]) => this.dataSource.data = d);
    });
  }

  printTicket(){
    window.print();
  }
}
