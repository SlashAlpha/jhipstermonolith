import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MeAppTestModule } from '../../../test.module';
import { SubTaskComponent } from 'app/entities/sub-task/sub-task.component';
import { SubTaskService } from 'app/entities/sub-task/sub-task.service';
import { SubTask } from 'app/shared/model/sub-task.model';

describe('Component Tests', () => {
  describe('SubTask Management Component', () => {
    let comp: SubTaskComponent;
    let fixture: ComponentFixture<SubTaskComponent>;
    let service: SubTaskService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MeAppTestModule],
        declarations: [SubTaskComponent]
      })
        .overrideTemplate(SubTaskComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubTaskComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SubTaskService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SubTask(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.subTasks && comp.subTasks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
