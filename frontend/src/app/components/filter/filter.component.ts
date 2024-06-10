import { Component } from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MovieModel} from "../../models/movie.model";
import {MovieService} from "../../services/movie.service";
import {NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf,
    RouterLink
  ],
  styleUrls: ['./filter.component.css']
})
export class FilterComponent {
  searchForm = new FormGroup({
    searchText: new FormControl(''),
    searchOption: new FormControl('title') // Значение по умолчанию
  });

  foundMovies: MovieModel[] = [];

  constructor(private movieService: MovieService) { }

  searchMovies(): void {
    const searchText: string = this.searchForm.get('searchText')?.value ?? '';
    const searchOption: string = this.searchForm.get('searchOption')?.value ?? '';

    if (searchText.trim() === '') {
      this.foundMovies = []; // Очищаем список фильмов при пустом запросе
      return;
    }

    switch (searchOption) {
      case 'person':
        this.movieService.getMoviesByPerson(searchText).subscribe({
          next: (data) => {
            this.foundMovies = data; // Возвращается один фильм по персоне
          },
          error: (err) => {
            console.error(err);
            this.foundMovies = [];
          }
        });
        break;
      case 'title':
        this.movieService.getMoviesByTitle(searchText).subscribe({
          next: (data:any) => {
            this.foundMovies = Array.isArray(data) ? data : [data];
          },
          error: (err:any) => {
            console.error(err);
            this.foundMovies = [];
          }
        });
        break;
      case 'genre':
        this.movieService.getMoviesByGenre(searchText).subscribe({
          next: (data) => {
            this.foundMovies = data;
          },
          error: (err) => {
            console.error(err);
            this.foundMovies = [];
          }
        });
        break;
      case 'country':
        this.movieService.getMoviesByCountry(searchText).subscribe({
          next: (data) => {
            this.foundMovies = data;
          },
          error: (err) => {
            console.error(err);
            this.foundMovies = [];
          }
        });
        break;
      default:
        this.foundMovies = [];
        break;
    }
  }
}
