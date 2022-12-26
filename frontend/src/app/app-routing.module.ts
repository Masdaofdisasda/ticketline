import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';

import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {EventComponent} from './components/event/event.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {EventCreateComponent} from './components/event/event-create/event-create.component';
import {LockedUsersComponent} from './components/locked-users/locked-users.component';

import {SeatSelectionComponent} from './components/order/seat-selection/seat-selection.component';
import {CheckoutComponent} from './components/order/checkout/checkout.component';
import {OrderCompleteComponent} from './components/order/order-complete/order-complete.component';
import {ArtistComponent} from './components/artist/artist.component';
import {ArtistCreateComponent} from './components/artist/artist-create/artist-create.component';
import {UserComponent} from './components/user/user/user.component';
import {BookingsComponent} from './components/user/bookings/bookings.component';

const routes: Routes = [
  {path: '', redirectTo: '/event', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'user', component: UserComponent},
  {path: 'user/bookings', component: BookingsComponent},
  {
    path: 'user/locked',
    canActivate: [AuthGuard],
    component: LockedUsersComponent,
  },
  {path: 'message', component: MessageComponent},

  {path: 'event', component: EventComponent},
  {path: 'event/create', component: EventCreateComponent},
  {path: 'event', component: EventComponent,},
  {path: 'event/:eventId/seats', component: SeatSelectionComponent},

  {path: 'artist', component: ArtistComponent},
  {path: 'artist/create', component: ArtistCreateComponent},

  {path: 'order/cart', component: CheckoutComponent, pathMatch: 'full'},
  {path: 'order/:bookingId', component: OrderCompleteComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
