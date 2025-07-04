import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { TecnicosService } from '../services/tecnicos.service';
import { TicketHistory } from '../models/ticket-history.model';

@Component({
  selector: 'app-ticket-details',
  templateUrl: './ticket-details.component.html',
  styleUrls: ['./ticket-details.component.css']
})
export class TicketDetailsComponent implements OnInit {
  histories: TicketHistory[] = [];
  dataSource = new MatTableDataSource<TicketHistory>();
  displayedColumns = ['timestamp','action','previousStatus','newStatus'];
  ticketId!: number;

  constructor(private route: ActivatedRoute, private service: TecnicosService){}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ticketId = +params['id'];
      this.service.getTicketHistory(this.ticketId).subscribe(data => {
        this.histories = data;
        this.dataSource.data = this.histories;
      });
    });
  }

  restore(){
    this.service.restoreTicket(this.ticketId).subscribe(()=>{
      this.service.getTicketHistory(this.ticketId).subscribe(d=>this.dataSource.data=d);
    });
  }
}
