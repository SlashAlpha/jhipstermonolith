import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'book',
        loadChildren: () => import('./book/book.module').then(m => m.MeAppBookModule)
      },
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.MeAppProjectModule)
      },
      {
        path: 'task',
        loadChildren: () => import('./task/task.module').then(m => m.MeAppTaskModule)
      },
      {
        path: 'sub-task',
        loadChildren: () => import('./sub-task/sub-task.module').then(m => m.MeAppSubTaskModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MeAppEntityModule {}
