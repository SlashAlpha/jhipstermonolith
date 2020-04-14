import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProject } from 'app/shared/model/project.model';
import { ITask } from 'app/shared/model/task.model';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

type EntityResponseType = HttpResponse<IProject>;
type EntityArrayResponseType = HttpResponse<IProject[]>;
type EntityResponseType1 = HttpResponse<ITask>;
type EntityArrayResponseType1 = HttpResponse<ITask[]>;

@Injectable({ providedIn: 'root' })
export class ProjectService {
  public resourceUrl = SERVER_API_URL + 'api/projects';

  constructor(protected http: HttpClient) {}

  create(project: IProject): Observable<EntityResponseType> {
    return this.http.post<IProject>(this.resourceUrl, project, { observe: 'response' });
  }

  update(project: IProject): Observable<EntityResponseType> {
    return this.http.put<IProject>(this.resourceUrl, project, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProject[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  createTask(task: ITask): Observable<EntityResponseType1> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .post<ITask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType1) => this.convertDateFromServer(res)));
  }

  updateTask(task: ITask): Observable<EntityResponseType1> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .put<ITask>(this.resourceUrl + '/task', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType1) => this.convertDateFromServer(res)));
  }

  findTask(id: number): Observable<EntityResponseType1> {
    return this.http
      .get<ITask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType1) => this.convertDateFromServer(res)));
  }

  queryTask(req?: any): Observable<EntityArrayResponseType1> {
    const options = createRequestOption(req);
    return this.http
      .get<ITask[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType1) => this.convertDateArrayFromServer(res)));
  }

  deleteTask(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(task: ITask): ITask {
    const copy: ITask = Object.assign({}, task, {
      startDate: task.startDate && task.startDate.isValid() ? task.startDate.format(DATE_FORMAT) : undefined,
      deadline: task.deadline && task.deadline.isValid() ? task.deadline.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType1): EntityResponseType1 {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.deadline = res.body.deadline ? moment(res.body.deadline) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType1): EntityArrayResponseType1 {
    if (res.body) {
      res.body.forEach((task: ITask) => {
        task.startDate = task.startDate ? moment(task.startDate) : undefined;
        task.deadline = task.deadline ? moment(task.deadline) : undefined;
      });
    }
    return res;
  }
}
