import {PersonModel} from "./person.model";
import {GenreModel} from "./genre.model";
import {CountryModel} from "./country.model";

export class MovieModel {
  id: number;
  name: string;
  description: string;
  type: string;
  typeNumber: number;
  status: number;
  statusId: number;
  persons: PersonModel[];
  genres: GenreModel[];
  countries: CountryModel[];
  constructor(
  id: number,
  name: string,
  description: string,
  type: string,
  typeNumber: number,
  status: number,
  statusId: number,
  persons: PersonModel[],
  genres: GenreModel[],
  countries: CountryModel[]) {
    this.id= id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.typeNumber = typeNumber;
    this.status = status;
    this.statusId = statusId;
    this.persons = persons;
    this.genres = genres;
    this.countries = countries;
  }
}
