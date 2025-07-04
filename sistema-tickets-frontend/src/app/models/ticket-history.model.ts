export interface TicketHistory {
  id: number;
  action: string;
  previousStatus: string | null;
  newStatus: string | null;
  timestamp: string;
}
