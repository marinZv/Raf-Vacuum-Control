import {VacuumAction} from "../vacuum-action";
import {VacuumResponse} from "./vacuum-response";

export interface VacuumErrorResponse{
  id: number,
  message: string,
  action: VacuumAction,
  vacuum: VacuumResponse,
  dateError: number
}
