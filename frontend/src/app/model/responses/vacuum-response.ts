import {VacuumStatus} from "../vacuum-status";

export interface VacuumResponse{
  id: number,
  name: string,
  status: VacuumStatus,
  createdDate: number
}
