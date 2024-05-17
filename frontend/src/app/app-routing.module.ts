import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UsersComponent} from "./components/users/users.component";
import {RoleEnum} from "./model/role-enum";
import {HomeComponent} from "./components/home/home.component";
import {TokenGuard} from "./guards/token.guard";
import {LoginComponent} from "./components/login/login.component";
import {CreateUserComponent} from "./components/create-user/create-user.component";
import {UpdateUserComponent} from "./components/update-user/update-user.component";
import {SearchVacuumsComponent} from "./components/search-vacuums/search-vacuums.component";
import {VacuumErrorComponent} from "./components/vacuum-error/vacuum-error.component";
import {ScheduleComponent} from "./components/schedule/schedule.component";
import {AddVacuumsComponent} from "./components/add-vacuums/add-vacuums.component";

const routes: Routes = [
  {
    path: "users",
    component: UsersComponent,
    canActivate: [TokenGuard],
    data: {roles: [RoleEnum.CAN_READ, RoleEnum.CAN_UPDATE, RoleEnum.CAN_DELETE]}
  },
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "create-user",
    component: CreateUserComponent,
    canActivate: [TokenGuard],
    data: {roles: [RoleEnum.CAN_CREATE]}
  },
  {
    path: "update-user/:userId",
    component: UpdateUserComponent,
    canActivate: [TokenGuard],
    data: {roles: [RoleEnum.CAN_UPDATE]}
  },
  {
    path: "search-vacuums",
    component: SearchVacuumsComponent,
    canActivate: [TokenGuard],
    data: {roles: [RoleEnum.CAN_SEARCH_VACUUM]}
  },
  {
    path: "vacuum-errors",
    component: VacuumErrorComponent,
    canActivate: [TokenGuard],
    data: {roles: [RoleEnum.CAN_CREATE]}
  },
  {
    path: "schedule/:vacuumId/:vacuumName",
    component: ScheduleComponent,
    // canActivate: [TokenGuard]
  },
  {
    path: "add-vacuums",
    component: AddVacuumsComponent,
    canActivate: [TokenGuard],
    data:{roles: [RoleEnum.CAN_ADD_VACUUM]}
  }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
