import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { MessageComponent } from './components/message/message.component';
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

const routes: Routes = [
  { path: '', redirectTo: '/event', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'user', component: UserComponent },
  { path: 'user/bookings', component: BookingsComponent },
  {
    path: 'admin/users',
    canActivate: [AdminGuard],
    component: UsersComponent,
  },
  { path: 'message', component: MessageComponent },
  { path: 'artist', component: ArtistComponent },
  { path: 'artist/create', component: ArtistCreateComponent },

  { path: 'event', component: EventComponent },
  { path: 'event/create', component: EventCreateComponent },
  { path: 'event', component: EventComponent },
  { path: 'event/:eventId/seats', component: SeatSelectionComponent },

  { path: 'artist', component: ArtistComponent },
  { path: 'artist/create', component: ArtistCreateComponent },

  { path: 'order/cart', component: CheckoutComponent, pathMatch: 'full' },
  { path: 'order/:bookingId', component: OrderCompleteComponent },
  { path: 'message', canActivate: [AuthGuard], component: MessageComponent },
  {
    path: 'venue',
    children: [
      {
        path: 'create',
        component: CreateVenueComponent,
        data: { edit: false },
      },
      { path: 'edit', component: CreateVenueComponent, data: { edit: true } },
    ],
  },
  { path: 'message', canActivate: [AuthGuard], component: MessageComponent },
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
  { path: 'admin/venue', component: VenueAdminComponent, pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
