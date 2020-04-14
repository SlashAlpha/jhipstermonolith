import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MeAppTestModule } from '../../../test.module';
import { SubTaskUpdateComponent } from 'app/entities/sub-task/sub-task-update.component';
import { SubTaskService } from 'app/entities/sub-task/sub-task.service';
import { SubTask } from 'app/shared/model/sub-task.model';

describe('Component Tests', () => {
  describe('SubTask Management Update Component', () => {
    let comp: SubTaskUpdateComponent;
    let fixture: ComponentFixture<SubTaskUpdateComponent>;
    let service: SubTaskService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MeAppTestModule],
        declarations: [SubTaskUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SubTaskUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubTaskUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SubTaskService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SubTask(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SubTask();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
