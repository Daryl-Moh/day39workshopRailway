import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from '../models/character';
import { MarvelCharService } from '../services/marvel-char.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent {
  charName = "";
  param$!: Subscription;
  characters!: Character[];
  currentIndex!: number;

  constructor(private activatedRoute: ActivatedRoute,
    private marvelCharSvc: MarvelCharService, private router: Router) {

  }

  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        this.charName = params['charName'];
        console.log('[ngOnInit] >>> this.charName = ' + this.charName);
        const l = await this.marvelCharSvc.getCharacters(this.charName, 0, 20);
        this.currentIndex = 1;
        console.log('[ngOnInit] >>> marvelCharSvc.getCharacters ' + l);
        if (l === undefined || l.length == 0) {
          this.router.navigate(['/'])
        } else {
          this.characters = l;
        }

      }
    );

  }

  async previous() {
    console.log('[previous] >>> this.currentIndex = ' + this.currentIndex);
    if (this.currentIndex > 0) {
      this.currentIndex = this.currentIndex + 20;
      const l = await this.marvelCharSvc
        .getCharacters(this.charName, this.currentIndex, 20);
      this.characters = l;
    }
    console.log('[previous] >>> this.currentIndex = ' + this.currentIndex);
  }

  async next() {
    console.log('[next] >>> this.currentIndex = ' + this.currentIndex);
    this.currentIndex = this.currentIndex + 20;
    const l = await this.marvelCharSvc
      .getCharacters(this.charName, this.currentIndex, 20);
    this.characters = l;
    console.log('[next] >>> this.currentIndex = ' + this.currentIndex);
  }

  ngOnDestroy(): void {
    console.log('[ngOnDestroy] >>> unsubscribe ');
    this.param$.unsubscribe();
  }
}
