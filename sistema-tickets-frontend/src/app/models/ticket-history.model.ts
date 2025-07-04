export interface TicketHistory {
  id: number;
  action: string;
  changes: string | null;
  previousStatus: string | null;
  newStatus: string | null;
  timestamp: string;
}
