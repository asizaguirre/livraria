import { Routes } from '@angular/router';
import { AssuntosListComponent } from './components/assuntos-list/assuntos-list.component';
import { AssuntoFormComponent } from './components/assunto-form/assunto-form.component';

export const ASSUNTOS_ROUTES: Routes = [
  { path: '', component: AssuntosListComponent },
  { path: 'new', component: AssuntoFormComponent },
  { path: 'edit/:id', component: AssuntoFormComponent }
];
