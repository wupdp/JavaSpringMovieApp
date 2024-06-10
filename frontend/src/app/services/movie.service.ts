// movie.service.ts
import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, of, throwError} from 'rxjs';
import {MovieModel} from "../models/movie.model";


@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private baseUrl = 'http://localhost:8088';

  constructor(private http: HttpClient) {
  }

  getAllMovies(): Observable<MovieModel[]> {
    return this.http.get<MovieModel[]>(`${this.baseUrl}/movie/all`);
  }
  getMoviesByTitle(title: string): any {
    return this.http.get<MovieModel[]>(`${this.baseUrl}/movie/byTitle?title=${title}`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 404) {
            // Если получен код 404, возвращаем массив обертку с Observable
            return this.http.get<MovieModel[]>(`${this.baseUrl}/movie/info?title=${title}`);

          } else {
            // В противном случае, выбрасываем ошибку
            return throwError(error);
          }
        })
      );
  }
  getMoviesByPerson(person: string): Observable<MovieModel[]> {
    return this.http.get<MovieModel[]>(`${this.baseUrl}/movie/byPerson?person=${person}`);
  }
  getMoviesByGenre(genre: string): Observable<MovieModel[]> {
    return this.http.get<MovieModel[]>(`${this.baseUrl}/movie/byGenre?genre=${genre}`);
  }
  getMoviesByCountry(country: string): Observable<MovieModel[]> {
    return this.http.get<MovieModel[]>(`${this.baseUrl}/movie/byCountry?country=${country}`);
  }
  getRandomMovie(): Observable<MovieModel> {
    return this.http.get<MovieModel>('http://localhost:8081/movie/random')
  }
  getRandomAnime(): Observable<MovieModel> {
    return this.http.get<MovieModel>('http://localhost:8081/movie/random/anime')
  }

  deleteMovie(movieId: number): Observable<MovieModel> {
    return this.http.delete<MovieModel>(`http://localhost:8000/movies/${movieId}`);
  }
}
