import {Component, OnInit} from '@angular/core';
import {MovieService} from "../../services/movie.service";
import {MovieModel} from "../../models/movie.model";
import {RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-all-movies',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    FormsModule
  ],
  templateUrl: './all-movies.component.html',
  styleUrl: './all-movies.component.css'
})
export class AllMoviesComponent implements OnInit{
  movies: MovieModel[] = [];

  constructor(private movieService: MovieService) {
  }

  ngOnInit(): void {
    this.retrieveMovies();
  }

  retrieveMovies(): void {
    this.movieService.getAllMovies().subscribe({
      next: (data) => {
        this.movies = data;
        console.log('Movies:', this.movies);
      },
      error: (e) => console.error(e)
    });
  }

  deleteMovie(movieId: number): void {
    this.movieService.deleteMovie(movieId).subscribe({
      next: () => {
        // Удалить фильм из списка
        this.movies = this.movies.filter(movie => movie.id !== movieId);
      },
      error: (e) => console.error('Error deleting movie:', e)
    });
  }
}
