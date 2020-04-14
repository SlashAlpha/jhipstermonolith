import { Moment } from 'moment';
import { ITask } from 'app/shared/model/task.model';

export interface ISubTask {
  id?: number;
  title?: string;
  description?: string;
  duration?: number;
  cost?: number;
  budget?: number;
  started?: boolean;
  difficulty?: string;
  startDate?: Moment;
  deadline?: Moment;
  contact?: string;
  documentContentType?: string;
  document?: any;
  task?: ITask;
}

export class SubTask implements ISubTask {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public duration?: number,
    public cost?: number,
    public budget?: number,
    public started?: boolean,
    public difficulty?: string,
    public startDate?: Moment,
    public deadline?: Moment,
    public contact?: string,
    public documentContentType?: string,
    public document?: any,
    public task?: ITask
  ) {
    this.started = this.started || false;
  }
}
