import { Moment } from 'moment';
import { ISubTask } from 'app/shared/model/sub-task.model';
import { IProject } from 'app/shared/model/project.model';

export interface ITask {
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
  subTasks?: ISubTask[];
  project?: IProject;
}

export class Task implements ITask {
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
    public subTasks?: ISubTask[],
    public project?: IProject
  ) {
    this.started = this.started || false;
  }
}
