import {Component, OnInit, ViewChild} from '@angular/core';
import {VacuumResponse} from "../../model/responses/vacuum-response";
import {VacuumStatus} from "../../model/vacuum-status";
import {PopupComponent} from "../popup/popup.component";
import {VacuumService} from "../../services/vacuum.service";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {RoleEnum} from "../../model/role-enum";
import {environment} from "../../../environments/environment";
import {CompatClient, Stomp} from "@stomp/stompjs";
import * as SockJS from "sockjs-client";
import {VacuumAction} from "../../model/vacuum-action";

@Component({
  selector: 'app-search-vacuums',
  templateUrl: './search-vacuums.component.html',
  styleUrls: ['./search-vacuums.component.css']
})
export class SearchVacuumsComponent implements OnInit{

  vacuums: VacuumResponse[] = [];
  canAdd: boolean = false;
  canRemove: boolean = false;
  canStart: boolean = false;
  canStop: boolean = false;
  canDischarge: boolean = false;

  vacuumName: string = "";
  statusList: VacuumStatus[] = [];
  dateFrom: number = -1;
  dateTo: number = -1;

  @ViewChild(PopupComponent)
  popupComponent!: PopupComponent


  stompClient!: CompatClient;

  constructor(private vacuumService: VacuumService, private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.getVacuums();
    this.checkAllRoles();
    this.connectToSocket();
  }

  checkAllRoles(){
    this.canAdd = this.userService.checkUserRole(RoleEnum.CAN_ADD_VACUUM);
    this.canRemove = this.userService.checkUserRole(RoleEnum.CAN_REMOVE_VACUUM);
    this.canStart = this.userService.checkUserRole(RoleEnum.CAN_START_VACUUM);
    this.canStop = this.userService.checkUserRole(RoleEnum.CAN_STOP_VACUUM);
    this.canDischarge = this.userService.checkUserRole(RoleEnum.CAN_DISCHARGE_VACUUM);
  }


  getVacuums(){
    this.vacuumService.getVacuums(this.vacuumName, this.statusList, this.dateFrom, this.dateTo).subscribe({
      complete: () => {
        this.statusList = [];
        this.dateTo = -1;
        this.dateFrom = -1;
        this.vacuumName = "";
        this.clearChecked();
      },
      error: (error) => {
        this.openPopup("Error", error.message);
      },
      next: (vacuums) => {
        this.vacuums = vacuums;
      }
    });
  }

  connectToSocket(){
    let jwt = localStorage.getItem("jwt");
    const socket = new SockJS(environment.wsUrl + "?jwt=" + jwt);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, this.onConnect.bind(this));

  }


  onConnect(){
    let email = this.userService.getUserEmail();
    if(email != null){
      this.stompClient.subscribe('/vacuum-fe/' + email, this.changeVacuums.bind(this));
    }
  }


  changeVacuums(vacuumMessage: any){
    let vacuum = JSON.parse(vacuumMessage.body);
    for(let v of this.vacuums){
      if(v.id == vacuum.id){
        v.status = vacuum.status;
      }
    }
  }

  searchVacuums(){
    let stopped = document.getElementById('stopped-check') as HTMLInputElement;
    let running = document.getElementById('running-check') as HTMLInputElement;

    if(stopped.checked){
      this.statusList.push(VacuumStatus.STOPPED)
    }
    if(running.checked){
      this.statusList.push(VacuumStatus.RUNNING)
    }

    let dateFrom = document.getElementById('date-from') as HTMLInputElement;
    let dateTo = document.getElementById('date-to') as HTMLInputElement;

    if(dateTo.value || dateFrom.value){
      if(!isNaN(dateFrom.valueAsNumber) && !isNaN(dateTo.valueAsNumber)){
        this.dateFrom = dateFrom.valueAsNumber;
        this.dateTo = dateTo.valueAsNumber;
      }else{
        this.openPopup("Error", "Both dates must be selected")
      }
    }

    this.getVacuums();
  }

  removeVacuum(vacuumId: number){
    this.vacuumService.removeVacuum(vacuumId).subscribe({
      complete: () => {},
      error: (error) => {
        this.openPopup("Error!", error.message);
      },
      next: () => {
        for(let i = 0; i < this.vacuums.length; i++){
          if(this.vacuums[i].id == vacuumId){
            this.vacuums.splice(i, 1);
            break;
          }
        }
      }
    });
  }

  startVacuum(vacuumId: number){
    this.vacuumService.vacuumActions(vacuumId, VacuumAction.START).subscribe({
      complete: () => {},
      error: (error) => {
        //this.openPopup("Error!", error.message);
        let message: string = "Check error page, you cause an error!";
        this.openPopup("Error!", message)
      },
      next: () => {}
    })
  }


  stopVacuum(vacuumId: number){
    this.vacuumService.vacuumActions(vacuumId, VacuumAction.STOP).subscribe({
      complete: () => {},
      error: (error) => {
        let message: string = "Check error page, you cause an error!";
        this.openPopup("Error!", message)
        // this.openPopup("Error!", error.message);
      },
      next: () => {}
    })
  }




  dischargeVacuum(vacuumId: number){
    this.vacuumService.vacuumActions(vacuumId, VacuumAction.DISCHARGE).subscribe({
      complete: () => {},
      error: (error) => {
        // this.openPopup("Error!", error.message);
        let message: string = "Check error page, you cause an error!";
        this.openPopup("Error!", message)
      },
      next: () => {}
    });
  }

  isStopped(status: VacuumStatus): boolean{
    return status == VacuumStatus.STOPPED;
  }

  openSchedule(vacuumId: number, vacuumName: string){
    this.router.navigate(['/schedule', vacuumId, vacuumName]);
  }

  clearChecked(){
    let stopped = document.getElementById('stopped-check') as HTMLInputElement;
    let running = document.getElementById('running-check') as HTMLInputElement;

    stopped.checked = false;
    running.checked = false;
  }

  private openPopup(title: string, message: string){
    this.popupComponent.title = title;
    this.popupComponent.message = message;
    this.popupComponent.displayStyle = "block";
  }

}
