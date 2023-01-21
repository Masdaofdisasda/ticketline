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
    this.artistService.create({
      name: this.createFormGroup.get('artistName').value,
    } as ArtistDto).subscribe(() => this.router.navigate(['/artist']));
  }
}
