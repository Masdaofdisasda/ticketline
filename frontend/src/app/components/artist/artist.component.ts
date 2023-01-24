import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {ArtistDto} from '../../dto/artist.dto';
import {ArtistService} from '../../services/artist.service';

@Component({
  selector: 'app-artist',
  templateUrl: './artist.component.html',
  styleUrls: ['./artist.component.scss']
})
export class ArtistComponent implements OnInit {

  artists: ArtistDto[] = [];
  constructor(public auth: AuthService, private artistService: ArtistService) {
  }

  ngOnInit(): void {
    this.artistService.findAll().subscribe({
      next: data =>{
        this.artists = data;
      }
    });
  }

}
