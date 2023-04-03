import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  form!: FormGroup;
  charName?: string;

  constructor(private formBuilder: FormBuilder, private router: Router,
  ) {

  }

  ngOnInit(): void {
    this.form = this.createForm();
  }

  ngOnDestroy(): void {

  }

  search() {
    const charName = this.form?.value['charName'];
    console.log('[search] navigating to /list >>> charName = ' + charName);
    this.router.navigate(['/list', charName]);
  }

  private createForm(): FormGroup {
    return this.formBuilder.group({
      charName: this.formBuilder.control('', [Validators.required]),
    })
  }
}
