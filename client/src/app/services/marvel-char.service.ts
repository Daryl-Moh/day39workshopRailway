import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Character } from "../models/character";
import { Comment } from "../models/comment";

@Injectable({
    providedIn: 'root'
  })
  export class MarvelCharService {
    private API_URI: string = "/api/characters";
  
    constructor(private httpClient: HttpClient) { }
  
    getCharacters(charName: string, offset: number, limit: number): Promise<any>{
      const params = new HttpParams()
          .set("characterName", charName)
          .set("limit", limit)
          .set("offset", offset);
  
      const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
      console.log('[getCharacters] >>> params = ' + params);
      console.log('[getCharacters] >>> headers = ' + headers);
    
      return lastValueFrom(this.httpClient
          .get<Character[]>(this.API_URI, {params: params, headers: headers}));
    }
  
  
    getCharactersDetails(charId: string): Promise<any>{
      const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
      console.log('[getCharacterDetails] >>> headers = ' + headers);
      return lastValueFrom(this.httpClient
          .get<Character[]>(this.API_URI+"/" + charId, {headers: headers}));
    }
  
    saveComment(c:Comment) : Promise<any>{
      const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
      const body=JSON.stringify(c);
      console.log('[saveComent] >>> ' + body);
      return lastValueFrom(this.httpClient.post<Comment>(this.API_URI+"/" + c.id, body, {headers: headers}));
    }
  
    getCharComments(charId: string): Promise<Comment[]>{
      const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
      console.log("get all comments !");
      return lastValueFrom(this.httpClient
          .get<Comment[]>(this.API_URI+"/comments/" + charId, {headers: headers}));
    }
  
  }