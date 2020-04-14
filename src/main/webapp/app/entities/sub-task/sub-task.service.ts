import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISubTask } from 'app/shared/model/sub-task.model';

type EntityResponseType = HttpResponse<ISubTask>;
type EntityArrayResponseType = HttpResponse<ISubTask[]>;

@Injectable({ providedIn: 'root' })
export class SubTaskService {
  public resourceUrl = SERVER_API_URL + 'api/sub-tasks';

  constructor(protected http: HttpClient) {}

  create(subTask: ISubTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subTask);
    return this.http
      .post<ISubTask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(subTask: ISubTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subTask);
    return this.http
      .put<ISubTask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISubTask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubTask[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(subTask: ISubTask): ISubTask {
    const copy: ISubTask = Object.assign({}, subTask, {
      startDate: subTask.startDate && subTask.startDate.isValid() ? subTask.startDate.format(DATE_FORMAT) : undefined,
      deadline: subTask.deadline && subTask.deadline.isValid() ? subTask.deadline.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.deadline = res.body.deadline ? moment(res.body.deadline) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((subTask: ISubTask) => {
        subTask.startDate = subTask.startDate ? moment(subTask.startDate) : undefined;
        subTask.deadline = subTask.deadline ? moment(subTask.deadline) : undefined;
      });
    }
    return res;
  }
}
