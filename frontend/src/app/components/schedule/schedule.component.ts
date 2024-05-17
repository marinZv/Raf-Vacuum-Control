import {Component, OnInit, ViewChild} from '@angular/core';
import {VacuumAction} from "../../model/vacuum-action";
import {PopupComponent} from "../popup/popup.component";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {VacuumService} from "../../services/vacuum.service";
import {RoleEnum} from "../../model/role-enum";

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit{

  vacuumId: number = -1;
  vacuumName: string = "";
  allowedActions: VacuumAction[] = [];

  @ViewChild(PopupComponent)
  popupComponent!: PopupComponent

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private userService: UserService, private vacuumService: VacuumService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.vacuumId = +params['vacuumId'];
      this.vacuumName = params['vacuumName'];
    })

    this.getAllowedActions()
  }

  getAllowedActions(){
    if(this.userService.checkUserRole(RoleEnum.CAN_START_VACUUM)){
      this.allowedActions.push(VacuumAction.START);
    }
    if(this.userService.checkUserRole(RoleEnum.CAN_STOP_VACUUM)){
      this.allowedActions.push(VacuumAction.STOP);
    }
    if(this.userService.checkUserRole(RoleEnum.CAN_DISCHARGE_VACUUM)){
      this.allowedActions.push(VacuumAction.DISCHARGE);
    }

  }

  returnToVacuums(){
    this.router.navigate(['search-vacuums']);
  }

  scheduleTask(){
    let scheduleDate = document.getElementById('date-schedule') as HTMLInputElement;
    let scheduleTime = document.getElementById('time-schedule') as HTMLInputElement;

    console.log(`scheduleDate: ${scheduleDate.valueAsNumber}, scheduleTime: ${scheduleTime.valueAsNumber}`);

    if(!isNaN(scheduleDate.valueAsNumber) && !isNaN(scheduleTime.valueAsNumber)){
      let date = new Date(scheduleDate.value + " " + scheduleTime.value);
      console.log("Datum izvrsavanja je: " + date);
      let timestamp = date.getTime();

      console.log("usao sam u scheduleTask u ScheduleComponent.ts i date je: " + timestamp);

      let action = this.getSelectedAction();

      this.vacuumService.scheduleTask(this.vacuumId, action, timestamp).subscribe({
        complete: () => {
          this.returnToVacuums();
        },
        error: (error) => {
          let message: string = "You cause an error, check Vacuum Errors page!"
          this.openPopup("Error", message);
        },
        next: () => {}

      });
    }else{
      this.openPopup("Error!", "Date and time must be selected.");
    }
  }


  private openPopup(title: string, message: string){
    this.popupComponent.title = title;
    this.popupComponent.message = message;
    this.popupComponent.displayStyle = "block";
  }

  private getSelectedAction(): VacuumAction{
    let e = (document.getElementById("action-select")) as HTMLSelectElement;
    let selected = e.selectedIndex;

    return <VacuumAction>e.options[selected].value
  }



}
