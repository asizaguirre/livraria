import { Routes } from '@angular/router';
import { LivrosListComponent } from './components/livros-list/livros-list.component';
import { LivroFormComponent } from './components/livro-form/livro-form.component';

export const LIVROS_ROUTES: Routes = [
  { path: '', component: LivrosListComponent },
  { path: 'new', component: LivroFormComponent },
  { path: 'edit/:id', component: LivroFormComponent }
];
