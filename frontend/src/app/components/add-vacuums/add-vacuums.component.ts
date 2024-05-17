import {Component, OnInit, ViewChild} from '@angular/core';
import {VacuumService} from "../../services/vacuum.service";
import {UserService} from "../../services/user.service";
import {RoleEnum} from "../../model/role-enum";
import {PopupComponent} from "../popup/popup.component";

@Component({
  selector: 'app-add-vacuums',
  templateUrl: './add-vacuums.component.html',
  styleUrls: ['./add-vacuums.component.css']
})
export class AddVacuumsComponent implements OnInit{

  canAdd: boolean = false;
  addVacuumName: string = "";

  @ViewChild(PopupComponent)
  popupComponent!: PopupComponent;

  constructor(private vacuumService: VacuumService, private userService: UserService) {

  }

  ngOnInit(): void {
    this.checkRole();
    console.log(this.canAdd);
  }

  private checkRole(){
    this.canAdd = this.userService.checkUserRole(RoleEnum.CAN_ADD_VACUUM);
  }

  addVacuum():void{
    this.vacuumService.addVacuum(this.addVacuumName).subscribe({
      complete: () => {
        this.addVacuumName = "";
      },
      error: (error) => {
        this.openPopup("Error", error.message);
      },
    });
  }

  private openPopup(title: string, message: string){
    this.popupComponent.title = title;
    this.popupComponent.message = message;
    this.popupComponent.displayStyle = "block";
  }

}
