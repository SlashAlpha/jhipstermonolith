import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { MeAppTestModule } from '../../../test.module';
import { SubTaskDetailComponent } from 'app/entities/sub-task/sub-task-detail.component';
import { SubTask } from 'app/shared/model/sub-task.model';

describe('Component Tests', () => {
  describe('SubTask Management Detail Component', () => {
    let comp: SubTaskDetailComponent;
    let fixture: ComponentFixture<SubTaskDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ subTask: new SubTask(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MeAppTestModule],
        declarations: [SubTaskDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SubTaskDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubTaskDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load subTask on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subTask).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
