import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { NewsComponent } from './components/news/news.component';
import { CreateVenueComponent } from './components/venue-admin/create-venue/create-venue.component';
import { VenueAdminComponent } from './components/venue-admin/venue-admin.component';
import { ShowVenueComponent } from './components/venue-admin/show-venue/show-venue.component';
import { EventComponent } from './components/event/event.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { EventCreateComponent } from './components/event/event-create/event-create.component';
import { UsersComponent } from './components/users/users.component';

import { SeatSelectionComponent } from './components/order/seat-selection/seat-selection.component';
import { CheckoutComponent } from './components/order/checkout/checkout.component';
import { OrderCompleteComponent } from './components/order/order-complete/order-complete.component';
import { ArtistComponent } from './components/artist/artist.component';
import { ArtistCreateComponent } from './components/artist/artist-create/artist-create.component';
import { UserComponent } from './components/user/user/user.component';
import { BookingsComponent } from './components/user/bookings/bookings.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { PasswordForgotComponent } from './components/password-forgot/password-forgot.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { SelectPerformanceComponent } from './components/event/select-performance/select-performance.component';
import { NewsCreateComponent } from './components/news/news-create/news-create.component';

const routes: Routes = [
  { path: '', redirectTo: '/event', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'recover', component: PasswordForgotComponent },
  { path: 'user', component: UserComponent },
  { path: 'user/bookings', component: BookingsComponent },
  { path: 'user/changePassword', component: ChangePasswordComponent },
  {
    path: 'user/create',
    canActivate: [AdminGuard],
    component: CreateUserComponent,
  },
  {
    path: 'admin/users',
    canActivate: [AdminGuard],
    component: UsersComponent,
  },
  {path: 'news', component: NewsComponent},
  {path: 'news/create', component: NewsCreateComponent, canActivate: [AdminGuard]},
  { path: 'artist', component: ArtistComponent },
  { path: 'artist/create', component: ArtistCreateComponent },

  { path: 'event', component: EventComponent },
  { path: 'event/create', component: EventCreateComponent },
  { path: 'event', component: EventComponent },
  { path: 'event/:eventId/performances', component: SelectPerformanceComponent },
  { path: 'event/performance/:performanceId/seats', component: SeatSelectionComponent },

  { path: 'artist', component: ArtistComponent },
  { path: 'artist/create', component: ArtistCreateComponent },

  { path: 'order/cart', component: CheckoutComponent, pathMatch: 'full' },
  { path: 'order/:bookingId', component: OrderCompleteComponent },
  {
    path: 'admin/venue/create',
    component: CreateVenueComponent,
    data: { edit: false },
  },
  {
    path: 'admin/venue/edit/:id',
    component: CreateVenueComponent,
    data: { edit: true },
  },
  { path: 'admin/venue/show/:id', component: ShowVenueComponent },
  {
    path: 'admin/venue',
    canActivate: [AdminGuard],
    component: VenueAdminComponent,
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
