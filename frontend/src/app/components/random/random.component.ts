import { Component } from '@angular/core';
import {MovieModel} from "../../models/movie.model";
import {MovieService} from "../../services/movie.service";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-random',
  standalone: true,
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './random.component.html',
  styleUrl: './random.component.css'
})
export class RandomComponent {
  movie: MovieModel | null = null;
  anime: MovieModel | null = null;

  constructor(private movieService: MovieService) {}

  getRandomMovie(): void {
    this.movieService.getRandomMovie().subscribe({
      next: (data) => {
        this.movie = data; // Присваиваем полученный случайный фильм
      },
      error: (err) => {
        console.error(err);
        this.movie = null;
      }
    });
  }
  getRandomAnime(): void {
    this.movieService.getRandomAnime().subscribe({
      next: (data) => {
        this.anime = data; // Присваиваем полученный случайный фильм
      },
      error: (err) => {
        console.error(err);
        this.anime = null;
      }
    });
  }
}
