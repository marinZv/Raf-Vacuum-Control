import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { UsersComponent } from './components/users/users.component';
import { PopupComponent } from './components/popup/popup.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { UpdateUserComponent } from './components/update-user/update-user.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ScheduleComponent } from './components/schedule/schedule.component';
import { VacuumErrorComponent } from './components/vacuum-error/vacuum-error.component';
import { SearchVacuumsComponent } from './components/search-vacuums/search-vacuums.component';
import { AddVacuumsComponent } from './components/add-vacuums/add-vacuums.component';
import {ErrorInterceptor} from "../error-interceptor";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    UsersComponent,
    PopupComponent,
    CreateUserComponent,
    UpdateUserComponent,
    ScheduleComponent,
    VacuumErrorComponent,
    SearchVacuumsComponent,
    AddVacuumsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor() {
  }

}
