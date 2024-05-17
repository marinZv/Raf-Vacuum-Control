import {Component, ViewChild} from '@angular/core';
import {PopupComponent} from "../popup/popup.component";
import {UserService} from "../../services/user.service";
import {RoleEnum} from "../../model/role-enum";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {

  create: boolean = false;
  read: boolean = false;
  update: boolean = false;
  delete: boolean = false;

  startVacuum: boolean = false;
  stopVacuum: boolean = false;
  dischargeVacuum: boolean = false;
  searchVacuum: boolean = false;
  addVacuum:  boolean = false;
  removeVacuum: boolean = false;

  firstName: string = "";
  lastName: string = "";
  email: string = "";
  password: string = "";
  emailRegex: RegExp = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;

  @ViewChild(PopupComponent)
  popupComponent!: PopupComponent;

  constructor(private userService: UserService) { }

  ngOnInit(): void {

  }

  createUser(){
    if(this.checkNameAndSurname() && this.checkEmail() && this.checkPassword()){
      this.userService.createUser(this.firstName, this.lastName, this.email, this.password, this.getRoles())
        .subscribe((user) => {
          if(user != null) {
            this.openPopup("OK", "User created!");
          }
          this.clearForm();
        }, error => {
          this.openPopup("Error!", error.message);
        });
    }
    else {
      this.openPopup("Error!", "Invalid input.");
    }
  }

  private getRoles(): string[] {
    let roles: string[] = [];

    if(this.create) {
      roles.push(RoleEnum.CAN_CREATE);
    }
    if(this.read) {
      roles.push(RoleEnum.CAN_READ);
    }
    if(this.update) {
      roles.push(RoleEnum.CAN_UPDATE);
    }
    if(this.delete) {
      roles.push(RoleEnum.CAN_DELETE);
    }
    if(this.addVacuum){
      roles.push(RoleEnum.CAN_ADD_VACUUM);
    }
    if(this.removeVacuum){
      roles.push(RoleEnum.CAN_REMOVE_VACUUM);
    }
    if(this.startVacuum){
      roles.push(RoleEnum.CAN_START_VACUUM);
    }
    if(this.stopVacuum){
      roles.push(RoleEnum.CAN_STOP_VACUUM);
    }
    if(this.dischargeVacuum){
      roles.push(RoleEnum.CAN_DISCHARGE_VACUUM);
    }
    if(this.searchVacuum){
      roles.push(RoleEnum.CAN_SEARCH_VACUUM);
    }

    return roles;
  }

  private checkNameAndSurname(): boolean{
    return this.firstName != "" && this.lastName != "";
  }

  private checkEmail(): boolean{
    return this.emailRegex.test(this.email);
  }

  private checkPassword(): boolean{
    return this.password.length >= 4 && this.password.length <= 20;
  }

  private openPopup(title: string, message: string) {
    this.popupComponent.title = title;
    this.popupComponent.message = message;
    this.popupComponent.displayStyle="block";
  }

  private clearForm(){
    this.firstName = "";
    this.lastName = "";
    this.email = "";
    this.password = "";

    this.create = false;
    this.read = false;
    this.update = false;
    this.delete = false;

    this.addVacuum = false;
    this.removeVacuum = false;
    this.stopVacuum = false;
    this.startVacuum = false;
    this.dischargeVacuum = false;
    this.searchVacuum = false;
  }

}
