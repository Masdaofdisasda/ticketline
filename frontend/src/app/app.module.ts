import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {EventComponent} from './components/event/event.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastrModule} from 'ngx-toastr';
import {EventSearchComponent} from './components/event/event-search/event-search.component';
import {EventSearchResultComponent} from './components/event/event-search-result/event-search-result.component';
import {EventCreateComponent} from './components/event/event-create/event-create.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {LockedUsersComponent} from './components/locked-users/locked-users.component';
import {DateTimePickerComponent} from './components/shared/date-time-picker/date-time-picker.component';

import {SeatSelectionComponent} from './components/order/seat-selection/seat-selection.component';
import {ShoppingCartComponent} from './components/order/shopping-cart/shopping-cart.component';
import {TicketItemComponent} from './components/order/ticket-item/ticket-item.component';
import {CheckoutComponent} from './components/order/checkout/checkout.component';
import {OrderCompleteComponent} from './components/order/order-complete/order-complete.component';
import {ArtistComponent} from './components/artist/artist.component';
import {ArtistCreateComponent} from './components/artist/artist-create/artist-create.component';
import {UserComponent} from './components/user/user/user.component';
import {BookingsComponent} from './components/user/bookings/bookings.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    MessageComponent,
    RegistrationComponent,
    EventComponent,
    EventSearchComponent,
    EventSearchResultComponent,
    DateTimePickerComponent,
    RegistrationComponent,
    LockedUsersComponent,
    SeatSelectionComponent,
    ShoppingCartComponent,
    TicketItemComponent,
    CheckoutComponent,
    OrderCompleteComponent,
    EventCreateComponent,
    ArtistComponent,
    ArtistCreateComponent,
    UserComponent,
    BookingsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    })
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent],
})
export class AppModule {
}
