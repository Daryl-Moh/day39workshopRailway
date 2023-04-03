import { Component } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from '../models/character';
import { MarvelCharService } from '../services/marvel-char.service';
import { Comment } from '../models/comment';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent {
  charId = "";
  param$!: Subscription;
  character!: Character;
  comments!: Comment[];

  constructor(private activatedRoute: ActivatedRoute,
    private marvelCharSvc: MarvelCharService, private router: Router
    ) {

  }

  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        this.charId = params['charId'];
        console.log('[ngOnInt - DetailsComponent] >>> this.charId = ' + this.charId);
        const l = await this.marvelCharSvc.getCharactersDetails(this.charId);
        this.character = l.details;
        console.log('[ngOnInt - DetailsComponent] >>> marvelCharSvc.getCharactersDetails = ' + l.details);
        const ll = await this.marvelCharSvc.getCharComments(this.charId);
        console.log('[ngOnInt - DetailsComponent] >>> marvelCharSvc.getCharComments = ' + ll);
        this.comments = ll;

      }
    );

  }

  addComent() {
    const queryParams: Params = { charParam: this.character['name'] + '|' + this.character.id };
    console.log('[addComent] navigating to /comment >>> queryParams = ' + this.character['name'] + '|' + this.character.id );
    this.router.navigate(['/comment'], { queryParams: queryParams })
  }

  ngOnDestroy(): void {
    console.log('[ngOnDestroy]  >>> unsubscribe ')
    this.param$.unsubscribe();
  }
}
