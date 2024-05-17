import {Component, OnInit} from '@angular/core';
import {VacuumErrorResponse} from "../../model/responses/vacuum-error-response";
import {VacuumService} from "../../services/vacuum.service";

@Component({
  selector: 'app-vacuum-error',
  templateUrl: './vacuum-error.component.html',
  styleUrls: ['./vacuum-error.component.css']
})
export class VacuumErrorComponent implements OnInit{

  vacuumErrors: VacuumErrorResponse[] = [];

  constructor(private vacuumService: VacuumService) {
  }

  ngOnInit(): void {
    this.getVacuumErrors();
  }

  getVacuumErrors(){
    this.vacuumService.getVacuumErrors().subscribe((errors) => {
      if(errors.length > 0){
        this.vacuumErrors = errors;
        this.vacuumErrors.reverse();
      }
    })
  }

}
