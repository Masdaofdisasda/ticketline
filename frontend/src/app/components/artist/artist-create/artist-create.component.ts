import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ArtistService} from '../../../services/artist.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {ArtistDto} from '../../../dto/artist.dto';

@Component({
  selector: 'app-artist-create',
  templateUrl: './artist-create.component.html',
  styleUrls: ['./artist-create.component.scss']
})
export class ArtistCreateComponent implements OnInit {

  createFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private artistService: ArtistService,
              private router: Router, private notification: ToastrService) {
  }

  ngOnInit(): void {
    this.createFormGroup = this.formBuilder.group({
      artistName: new FormControl('', Validators.required),
    });
  }

  public onSubmit(): void {
    const observable = this.artistService.create({
      name: this.createFormGroup.get('artistName').value,
    } as ArtistDto);

    observable.subscribe({
      next: data => {
        this.notification.success(`Artist: \'${data.name}\' successfully created.`);
        this.router.navigate(['/artist']);
      },
      error: err => {
        console.log('Error creating Artist', err);
        console.log(err);
        this.notification.error('Error creating artist: \n' + err.error.errors);
      }
    });
  }

}
