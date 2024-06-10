import {RouterModule, Routes} from '@angular/router';
import {NgModule} from "@angular/core";
import {HomeComponent} from "./components/home/home.component";
import {AllMoviesComponent} from "./components/all-movies/all-movies.component";
import {FilterComponent} from "./components/filter/filter.component";
import {RandomComponent} from "./components/random/random.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'movies', component: AllMoviesComponent},
  {path: 'filter-movie', component: FilterComponent},
  {path: 'random-movie', component: RandomComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
