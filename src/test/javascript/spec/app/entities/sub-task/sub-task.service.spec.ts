import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SubTaskService } from 'app/entities/sub-task/sub-task.service';
import { ISubTask, SubTask } from 'app/shared/model/sub-task.model';

describe('Service Tests', () => {
  describe('SubTask Service', () => {
    let injector: TestBed;
    let service: SubTaskService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubTask;
    let expectedResult: ISubTask | ISubTask[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SubTaskService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SubTask(
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        0,
        false,
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        'image/png',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            deadline: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SubTask', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            deadline: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            deadline: currentDate
          },
          returnedFromService
        );

        service.create(new SubTask()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SubTask', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            duration: 'BBBBBB',
            cost: 1,
            budget: 1,
            started: true,
            difficulty: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            deadline: currentDate.format(DATE_FORMAT),
            contact: 'BBBBBB',
            document: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            deadline: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SubTask', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            duration: 'BBBBBB',
            cost: 1,
            budget: 1,
            started: true,
            difficulty: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            deadline: currentDate.format(DATE_FORMAT),
            contact: 'BBBBBB',
            document: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            deadline: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SubTask', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
